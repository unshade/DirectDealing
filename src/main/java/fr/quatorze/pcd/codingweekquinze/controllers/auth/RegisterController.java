package fr.quatorze.pcd.codingweekquinze.controllers.auth;

import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
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
    private TextField email;

    @FXML
    private void submit() {

        String enteredFirstName = this.firstName.getText();
        String enteredLastName = this.lastName.getText();
        String enteredPassword = this.password.getText();
        String enteredConfirmPassword = this.confirmPassword.getText();
        String enteredEmail = this.email.getText();

        if (enteredFirstName.isEmpty() || enteredPassword.isEmpty() || enteredConfirmPassword.isEmpty() || enteredLastName.isEmpty()) {
            LayoutManager.alert("Veuillez remplir tous les champs");
            return;
        }

        if (!enteredPassword.equals(enteredConfirmPassword)) {
            LayoutManager.alert("Les mots de passe ne correspondent pas");
            return;
        }

        String hashedPassword = AuthService.hashPassword(enteredPassword);
        System.out.println(hashedPassword);
        User user = UserDAO.getInstance().createUser(enteredFirstName, enteredLastName, enteredEmail, hashedPassword, 0, false, false);

        if (AuthService.getInstance().authenticate(user.getEmail(), enteredPassword)) {
            LayoutManager.addNavBar();
            LayoutManager.setLayout("borrow/index.fxml", "login");
        } else {
            LayoutManager.alert("Erreur lors de l'authentification");
        }
    }

    @FXML
    private void login() {
        LayoutManager.setLayout("auth/login.fxml", "Login");
    }
}
