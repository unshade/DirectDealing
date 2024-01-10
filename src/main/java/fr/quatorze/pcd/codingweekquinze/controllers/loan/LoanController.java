package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.controllers.MessageController;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class LoanController {

    private Loan loan;
    @FXML
    private VBox message;

    @FXML
    private MessageController messageController;
    private VBox rightPart;
    private Button acceptButton;
    private Button cancelButton;
    private Button finishButton;

    public LoanController(Loan loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        messageController = (MessageController) message.getProperties().get("controller");
        messageController.init(loan);
        if (this.loan.getStatus() == 0) {
            this.acceptButton = new Button("Accepter");
            acceptButton.setOnAction(event -> accept());
            rightPart.getChildren().add(acceptButton);
            this.cancelButton = new Button("Annuler");
            cancelButton.setOnAction(event -> cancel());
            rightPart.getChildren().add(cancelButton);
        } else if (this.loan.getStatus() == 1) {
            this.finishButton = new Button("Terminer");
            finishButton.setOnAction(event -> finish());
            rightPart.getChildren().add(finishButton);
        }
    }

    @FXML
    private void accept() {
        loan.accept();
        acceptButton.setVisible(false);
        cancelButton.setVisible(false);
        this.finishButton = new Button("Terminer");
        finishButton.setOnAction(event -> finish());
        rightPart.getChildren().add(finishButton);
    }

    @FXML
    public void cancel() {
        loan.cancel();
        acceptButton.setVisible(false);
        cancelButton.setVisible(false);
    }

    @FXML
    private void finish() {
        loan.end();
        finishButton.setVisible(false);
        User borrower = loan.getBorrower();
        User owner = loan.getItem().getOwner();
        borrower.pay(owner, loan.getItem().getPrice());
    }
}
