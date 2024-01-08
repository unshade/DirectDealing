package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.layout.LayoutManager;
import javafx.fxml.FXML;

public class LoginController {


    @FXML
    private void register() {
        LayoutManager.setLayout("register.fxml", "Register");
    }
}
