package fr.quatorze.pcd.codingweekquinze.controllers.auth;

import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    public VBox stack;
    @FXML
    public MFXButton connectButton;

    @FXML
    private MFXTextField firstName;
    @FXML
    private MFXTextField lastName;
    @FXML
    private MFXPasswordField password;
    @FXML
    private MFXPasswordField confirmPassword;
    @FXML
    private MFXTextField email;

    @FXML
    private void initialize() {
        this.email.setOnKeyPressed(this::handleEnterPressed);
        this.password.setOnKeyPressed(this::handleEnterPressed);
        this.confirmPassword.setOnKeyPressed(this::handleEnterPressed);
        this.firstName.setOnKeyPressed(this::handleEnterPressed);
        this.lastName.setOnKeyPressed(this::handleEnterPressed);
    }

    private void handleEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            submit();
        }
    }

    @FXML
    private void submit() {

        String enteredFirstName = this.firstName.getText();
        String enteredLastName = this.lastName.getText();
        String enteredPassword = this.password.getText();
        String enteredConfirmPassword = this.confirmPassword.getText();
        String enteredEmail = this.email.getText();

        MFXProgressSpinner spinner = new MFXProgressSpinner();
        spinner.setRadius(18);
        stack.getChildren().remove(stack.getChildren().size() - 2);
        stack.getChildren().add(stack.getChildren().size() - 1, spinner);

        Runnable fix = () -> {
            stack.getChildren().remove(stack.getChildren().size() - 2);
            stack.getChildren().add(stack.getChildren().size() - 1, connectButton);
        };

        CompletableFuture.runAsync(() -> {
            if (enteredFirstName.isEmpty() || enteredPassword.isEmpty() || enteredConfirmPassword.isEmpty() || enteredLastName.isEmpty()) {
                Platform.runLater(() -> {
                    LayoutManager.alert("Veuillez remplir tous les champs");

                    fix.run();
                });
                return;
            }

            var pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
            if (!pattern.matcher(enteredEmail).matches()) {
                Platform.runLater(() -> {
                    LayoutManager.alert("Veuillez entrer une adresse email valide");

                    fix.run();
                });
                return;
            }

            if (!enteredPassword.equals(enteredConfirmPassword)) {
                Platform.runLater(() -> {
                    LayoutManager.alert("Les mots de passe ne correspondent pas");

                    fix.run();
                });
                return;
            }

            String hashedPassword = AuthService.hashPassword(enteredPassword);
            User user = UserDAO.getInstance().createUser(enteredFirstName, enteredLastName, enteredEmail, hashedPassword, 0, false, false);

            Platform.runLater(() -> {
                if (AuthService.getInstance().authenticate(user.getEmail(), enteredPassword)) {
                    LayoutManager.addNavBar();
                    LayoutManager.setLayout("borrow/index.fxml", "Se connecter");
                } else {
                    fix.run();

                    LayoutManager.alert("Erreur lors de l'authentification");
                }
            });
        });
    }

    @FXML
    private void login() {
        LayoutManager.setLayout("auth/login.fxml", "Se connecter");
    }
}
