package fr._14.pcd.codingweek15.controllers.auth;

import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    private void submit() {
        String enteredEmail = this.email.getText();
        String enteredPassword = this.password.getText();

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            LayoutManager.alert("Veuillez remplir tous les champs");
            return;
        }

        if (AuthService.getInstance().authenticate(enteredEmail, enteredPassword)) {
            LayoutManager.setLayout("auth/register.fxml", "login success");
            return;
        }

        LayoutManager.alert("Erreur lors de l'authentification");
    }

    @FXML
    private void register() {
        LayoutManager.setLayout("auth/register.fxml", "Register");
    }
}
