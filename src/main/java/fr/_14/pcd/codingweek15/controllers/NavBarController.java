package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.layout.LayoutManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NavBarController {

    @FXML
    private Label announcements;

    @FXML
    private Label myAnnouncements;

    @FXML
    private Label myLoans;

    @FXML
    private Label account;

    @FXML
    private void initialize() {
        announcements.setOnMouseClicked(event -> {
            LayoutManager.setLayout("register.fxml", "Annonces");
        });

        myAnnouncements.setOnMouseClicked(event -> {
            LayoutManager.setLayout("login.fxml", "Mes annonces");
        });

        myLoans.setOnMouseClicked(event -> {
            LayoutManager.setLayout("my_loans.fxml", "Mes emprunts");
        });

        account.setOnMouseClicked(event -> {
            LayoutManager.setLayout("account.fxml", "Mon compte");
        });
    }


}
