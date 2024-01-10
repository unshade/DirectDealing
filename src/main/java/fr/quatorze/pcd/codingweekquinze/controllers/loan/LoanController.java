package fr.quatorze.pcd.codingweekquinze.controllers.loan;

import fr.quatorze.pcd.codingweekquinze.controllers.MessageController;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class LoanController {

    private final Loan loan;

    @FXML
    private BorderPane root;

    @FXML
    private HBox actionSection;
    private Button acceptButton;
    private Button cancelButton;
    private Button finishButton;

    @FXML
    private Label title;
    @FXML
    private Label itemType;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemName;
    @FXML
    private Label borrower;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    public LoanController(Loan loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        FXMLLoaderUtil.inject("message.fxml", new MessageController(loan), node -> {
            root.setRight(node);
        });

        this.title.setText("Prêt de " + loan.getItem().getName());

        //TODO
        this.itemType.setText("Type : " + "Matériel");
        this.itemPrice.setText("Prix : " + loan.getItem().getPrice() + "€");
        this.itemName.setText("Nom : " + loan.getItem().getName());
        this.borrower.setText(loan.getBorrower().getFirstName() + " " + loan.getBorrower().getLastName());
        this.startDate.setText(loan.getStartDate().toString());
        this.endDate.setText(loan.getEndDate().toString());


        if (this.loan.getStatus() == 0) {
            this.acceptButton = new Button("Accepter");
            acceptButton.setOnAction(event -> accept());
            acceptButton.getStyleClass().add("btn-primary");
            actionSection.getChildren().add(acceptButton);
            this.cancelButton = new Button("Annuler");
            cancelButton.setOnAction(event -> cancel());
            cancelButton.getStyleClass().add("btn-cancel");
            actionSection.getChildren().add(cancelButton);
        } else if (this.loan.getStatus() == 1) {
            this.finishButton = new Button("Terminer");
            finishButton.setOnAction(event -> finish());
            finishButton.getStyleClass().add("btn-primary");
            actionSection.getChildren().add(finishButton);
        }
    }

    @FXML
    private void accept() {
        loan.accept();
        acceptButton.setVisible(false);
        cancelButton.setVisible(false);
        this.finishButton = new Button("Terminer");
        finishButton.setOnAction(event -> finish());
        actionSection.getChildren().add(0, finishButton);
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
