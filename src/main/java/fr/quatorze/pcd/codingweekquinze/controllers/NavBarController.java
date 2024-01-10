package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NavBarController implements Observer {

    @FXML
    public Label planning;
    @FXML
    private Label borrow;

    @FXML
    private Label loan;

    @FXML
    private Label account;

    @FXML
    private Label wallet;

    @FXML
    private HBox walletBox;

    public NavBarController() {
        AuthService.getInstance().getCurrentUser().addObserver(this);
    }

    @FXML
    private void initialize() {

        if (AuthService.getInstance().getCurrentUser() == null) {
            walletBox.setVisible(false);
        } else {
            wallet.setText(AuthService.getInstance().getCurrentUser().getFlow() + "⚘");
        }

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

    @FXML
    private void wallet() {
        LayoutManager.setLayout("wallet.fxml", "Mon porte-monnaie");
    }

    @FXML
    private void logout() {
        AuthService.getInstance().endSession();
        LayoutManager.removeNavBar();
        LayoutManager.setLayout("auth/login.fxml", "Login");
    }

    @FXML
    private void notifications() {
        LayoutManager.setLayout("notifications.fxml", "Notifications");
    }


    @Override
    public void update() {
        wallet.setText(AuthService.getInstance().getCurrentUser().getFlow() + "⚘");
    }
}
