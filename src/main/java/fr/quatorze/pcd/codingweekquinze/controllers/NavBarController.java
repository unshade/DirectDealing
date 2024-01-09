package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NavBarController {

    @FXML
    public Label planning;
    @FXML
    private Label borrow;

    @FXML
    private Label loan;

    @FXML
    private Label account;

    @FXML
    private void initialize() {
        borrow.setOnMouseClicked(event -> {
            LayoutManager.setLayout("borrow/index.fxml", "Emprunt");
        });

        loan.setOnMouseClicked(event -> {
            LayoutManager.setLayout("loan/index.fxml", "Prêt");
        });

        planning.setOnMouseClicked(event -> {
            LayoutManager.setLayout("planning.fxml", "Planning");
        });

        account.setOnMouseClicked(event -> {
            LayoutManager.setLayout("account.fxml", "Mon compte");
        });
    }


}
