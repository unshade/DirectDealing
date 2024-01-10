package fr.quatorze.pcd.codingweekquinze.controllers.auth;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    private void initialize() {
        this.email.setOnKeyPressed(this::handleEnterPressed);
        this.password.setOnKeyPressed(this::handleEnterPressed);
    }

    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            submit();
        }
    }

    @FXML
    private void submit() {
        String enteredEmail = this.email.getText();
        String enteredPassword = this.password.getText();

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            LayoutManager.alert("Veuillez remplir tous les champs");
            return;
        }

        if (AuthService.getInstance().authenticate(enteredEmail, enteredPassword)) {
            LayoutManager.addNavBar();
            LayoutManager.setLayout("borrow/index.fxml", "login success");
            return;
        }

        LayoutManager.alert("Erreur lors de l'authentification");
    }

    @FXML
    private void register() {
        LayoutManager.setLayout("auth/register.fxml", "Register");
    }
}
