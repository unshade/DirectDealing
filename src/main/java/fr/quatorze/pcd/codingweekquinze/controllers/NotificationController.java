package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.model.Notification;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.List;

public class NotificationController {

    @FXML
    public GridPane grid;

    @FXML
    private void initialize() {
        User current = AuthService.getInstance().getCurrentUser();
        List<Notification> notifications = current.getNotifications();
        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            Label label = new Label(notification.getMessage());
            grid.add(label, 0, i);
            Button viewed = new Button("Marquer comme lu");
            viewed.setOnAction(event -> {
                notification.markAsRead();
                grid.getChildren().removeAll(label, viewed);
            });
            grid.add(viewed, 1, i);
        }

    }

}
