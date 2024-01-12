package fr.quatorze.pcd.codingweekquinze.layout.component;

import fr.quatorze.pcd.codingweekquinze.controllers.Observable;
import fr.quatorze.pcd.codingweekquinze.controllers.Observer;
import fr.quatorze.pcd.codingweekquinze.model.Notification;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public final class NotificationButton extends MFXButton implements Observable {

    private final ContextMenu entriesPopup;
    private final List<Observer> observers = new LinkedList<>();

    public NotificationButton(Node graphic) {
        super("Notifications", graphic);
        this.entriesPopup = new ContextMenu();
        entriesPopup.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);" +
                "-fx-background-insets: 0, 1, 2; -fx-background-radius: 3px, 2px, 1px; -fx-padding: 3 4 3 4; -fx-text-fill: #242d35; -fx-font-size: 13px; -fx-font-weight: bold;");

        setListener();
    }

    public NotificationButton() {
        super("Notifications");
        this.entriesPopup = new ContextMenu();
        entriesPopup.setStyle("-fx-background-color: #ffffff; -fx-border-color: #000000; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-padding: 5px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);" +
                              "-fx-background-insets: 0, 1, 2; -fx-background-radius: 3px, 2px, 1px; -fx-padding: 3 4 3 4; -fx-text-fill: #242d35; -fx-font-size: 13px; -fx-font-weight: bold;");

        setListener();
    }

    private void setListener() {
        setOnAction(event -> {
            if (entriesPopup.isShowing()) {
                entriesPopup.hide();
            } else {
                User current = AuthService.getInstance().getCurrentUser();
                List<Notification> notifications = current.getNotifications().stream().filter(notification -> !notification.isRead())
                        .sorted(Comparator.comparing(Notification::getDate).reversed()).toList();
                if (!notifications.isEmpty()) {
                    populatePopup(notifications);
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(NotificationButton.this, Side.BOTTOM, 0, 0);
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((observableValue, oldValue, newValue) -> {
            entriesPopup.hide();
        });
    }

    private void populatePopup(List<Notification> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            Notification notification = searchResult.get(i);
            final String result = notification.getMessage();
            Label entryLabel = new Label();
            entryLabel.setGraphic(Styles.buildTextFlow("✗ " + result));
            entryLabel.setPrefHeight(10);
            entryLabel.setTextFill(Color.BLACK);
            entryLabel.setWrapText(true);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            item.setOnAction(actionEvent -> {
                notification.markAsRead();

                entriesPopup.getItems().remove(item);
                Platform.runLater(() -> entriesPopup.show(NotificationButton.this, Side.BOTTOM, 0, 0));

                notifyObservers();
            });
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(Observer::update);
    }
}