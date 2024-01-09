package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.dao.MessageDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.Message;
import fr.quatorze.pcd.codingweekquinze.model.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private Label owner;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;
    @FXML
    private Label item;

    private User currentUser;
    private User otherUser;
    private Loan loan;

    public MessageController(User currentUser, User otherUser, Loan loan) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
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

        owner.setText(loan.getItem().getOwner().getFirstName() + " " + loan.getItem().getOwner().getLastName());
        startDate.setText(loan.getStartDate().toString());
        endDate.setText(loan.getEndDate().toString());
        item.setText(loan.getItem().getName());
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

        new Thread(() -> scrollPane.setVvalue(1.0)).start();
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
