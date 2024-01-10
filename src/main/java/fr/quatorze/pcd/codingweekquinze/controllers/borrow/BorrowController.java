package fr.quatorze.pcd.codingweekquinze.controllers.borrow;

import fr.quatorze.pcd.codingweekquinze.controllers.MessageController;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class BorrowController {

    private final Loan loan;

    @FXML
    private BorderPane root;

    @FXML
    private HBox actionSection;

    @FXML
    private Label title;
    @FXML
    private Label itemType;
    @FXML
    private Label itemPrice;
    @FXML
    private Label itemName;
    @FXML
    private Label owner;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;

    public BorrowController(Loan loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        FXMLLoaderUtil.inject("message.fxml", new MessageController(loan), node -> {
            root.setRight(node);
        });

        this.title.setText("Emprunt de " + loan.getItem().getName());

        this.itemType.setText("Type : " + (loan.getItem().getIsService() ? "Service" : "Objet"));
        this.itemPrice.setText("Prix : " + loan.getItem().getPrice() + "€");
        this.itemName.setText("Nom : " + loan.getItem().getName());
        this.owner.setText(loan.getItem().getOwner().getFirstName() + " " + loan.getItem().getOwner().getLastName());
        this.startDate.setText(loan.getStartDate().toString());
        this.endDate.setText(loan.getEndDate().toString());

    }
}
