package fr.quatorze.pcd.codingweekquinze.layout.component;

import fr.quatorze.pcd.codingweekquinze.model.Notification;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;

public final class NotificationButton extends MFXButton {

    private final ContextMenu entriesPopup;

    public NotificationButton(Node graphic) {
        super("Notifications", graphic);
        this.entriesPopup = new ContextMenu();

        setListener();
    }

    public NotificationButton() {
        super("Notifications");
        this.entriesPopup = new ContextMenu();

        setListener();
    }

    private void setListener() {
        setOnAction(event -> {
            if (entriesPopup.isShowing()) {
                entriesPopup.hide();
            } else {
                User current = AuthService.getInstance().getCurrentUser();
                List<Notification> notifications = current.getNotifications();
                List<String> messages = notifications.stream().map(Notification::getMessage).toList();
                if (!messages.isEmpty()) {
                    populatePopup(messages);
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

    private void populatePopup(List<String> searchResult) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(searchResult.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final String result = searchResult.get(i);
            Label entryLabel = new Label();
            entryLabel.setGraphic(Styles.buildTextFlow(result));
            entryLabel.setPrefHeight(10);
            entryLabel.setTextFill(Color.BLACK);
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            menuItems.add(item);

            item.setOnAction(actionEvent -> {
                setText(result);
                entriesPopup.hide();
            });
        }

        entriesPopup.getItems().clear();
        entriesPopup.getItems().addAll(menuItems);
    }
}