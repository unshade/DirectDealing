package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.dao.MessageDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.Message;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

@RequiresAuth
public class MessageController {

    @FXML
    private VBox vBox;
    @FXML
    private TextArea textArea;
    @FXML
    private ScrollPane scrollPane;

    @FXML

    private User currentUser;
    private User otherUser;
    private Loan loan;

    public MessageController(Loan loan) {
        this.currentUser = AuthService.getInstance().getCurrentUser();
        if (currentUser == loan.getItem().getOwner()) {
            this.otherUser = loan.getBorrower();
        } else {
            this.otherUser = loan.getItem().getOwner();
        }
        this.loan = loan;
    }

    @FXML
    public void initialize() {
        // Remove scroll bar
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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
        textArea.clear();
        vBox.getChildren().add(vBox.getChildren().size() - 1, hBox);
        Platform.runLater(() -> {
            scrollPane.setVvalue(1.0);
        });
    }

    public void sendMessage() {
        String message = textArea.getText();
        if (message.isEmpty() || message.isBlank()) {
            return;
        }
        MessageDAO.getInstance().createMessage(message, currentUser, otherUser, loan);
        addMessage(message, Pos.CENTER_RIGHT);
    }



    public void back() {
        LayoutManager.setLayout("loan/my_loans.fxml", "Mes emprunts");
    }
}
