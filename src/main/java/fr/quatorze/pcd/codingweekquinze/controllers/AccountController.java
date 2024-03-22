package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.RequiresAuth;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@RequiresAuth
public class AccountController {

    @FXML
    private MFXTextField firstName;
    @FXML
    private MFXTextField lastName;
    @FXML
    private MFXPasswordField oldPassword;
    @FXML
    private MFXPasswordField password;
    @FXML
    private MFXPasswordField confirmPassword;
    @FXML
    private Label email;

    @FXML
    private void initialize() {
        User user = AuthService.getInstance().getCurrentUser();
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
    }

    @FXML
    private void edit() {

        String enteredFirstName = this.firstName.getText();
        String enteredLastName = this.lastName.getText();

        User user = AuthService.getInstance().getCurrentUser();
        user.setFirstName(enteredFirstName);
        user.setLastName(enteredLastName);
        UserDAO.getInstance().updateUser(user);


        LayoutManager.success("Modifications enregistrées");

    }

    @FXML
    private void submit() {

        String enteredOldPassword = this.oldPassword.getText();
        String enteredPassword = this.password.getText();
        String enteredConfirmPassword = this.confirmPassword.getText();

        if (!AuthService.getInstance().authenticate(AuthService.getInstance().getCurrentUser().getEmail(), enteredOldPassword)) {
            LayoutManager.alert("Mot de passe incorrect");
            return;
        }

        if (!enteredPassword.equals(enteredConfirmPassword)) {
            LayoutManager.alert("Les mots de passe ne correspondent pas");
            return;
        }

        String hashedPassword = AuthService.hashPassword(enteredPassword);
        User user = AuthService.getInstance().getCurrentUser();
        user.setPassword(hashedPassword);
        UserDAO.getInstance().updateUser(user);

        LayoutManager.success("Mot de passe modifié");

    }

    @FXML
    private void logout() {
        AuthService.getInstance().endSession();
        LayoutManager.removeNavBar();
        LayoutManager.setLayout("auth/login.fxml", "Se connecter");
    }

}
