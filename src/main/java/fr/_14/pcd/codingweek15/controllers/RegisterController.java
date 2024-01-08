package fr._14.pcd.codingweek15.controllers;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;

    @FXML
    private void submit() {

        String enteredFirstName = firstName.getText();
        String enteredLastName = lastName.getText();
        String enteredPassword = password.getText();
        String enteredConfirmPassword = confirmPassword.getText();

        if (enteredFirstName.isEmpty() || enteredPassword.isEmpty() || enteredConfirmPassword.isEmpty() || enteredLastName.isEmpty()) {
            LayoutManager.alert("Veuillez remplir tous les champs");
            return;
        }

        if (!enteredPassword.equals(enteredConfirmPassword)) {
            LayoutManager.alert("Les mots de passe ne correspondent pas");
            return;
        }

        String hashedPassword = AuthService.hashPassword(enteredPassword);
        User user = UserDAO.getInstance().createUser("John", "Doe", enteredFirstName, hashedPassword, 0, false, false);

        if (AuthService.getInstance().authenticate(user, enteredPassword)) {
            LayoutManager.setLayout("home.fxml", "Home");
        } else {
            LayoutManager.alert("Erreur lors de l'authentification");
        }
    }

    @FXML
    private void login() {
        LayoutManager.setLayout("login.fxml", "Login");
    }
}
