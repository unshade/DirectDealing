package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.dao.MessageDAO;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.Message;
import fr._14.pcd.codingweek15.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class MessageController {

    @FXML
    private VBox vBox;

    public MessageController(User currentUser, User otherUser, Loan loan) {
        if (currentUser == null || otherUser == null || loan == null) {
            // For testing purposes
            return;
        }

        MessageDAO messageDAO = MessageDAO.getInstance();
        List<Message> messagesFrom1 = messageDAO.search(new Message(currentUser, otherUser, loan, null));
        List<Message> messagesFrom2 = messageDAO.search(new Message(otherUser, currentUser, loan, null));

        for (Message message : messagesFrom1) {
            addMessage(message.getContent(), Pos.CENTER_RIGHT);
        }
        for (Message message : messagesFrom2) {
            addMessage(message.getContent(), Pos.CENTER_LEFT);
        }
    }

    private void addMessage(String message, Pos pos) {
        HBox hBox = new HBox();
        if (pos == Pos.CENTER_RIGHT) {
            Pane spacer = new Pane();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().add(spacer);
        }
        hBox.getChildren().add(new Label(message));
        hBox.setAlignment(pos);

        vBox.getChildren().add(hBox);
    }
}
