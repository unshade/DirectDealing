package fr.quatorze.pcd.codingweekquinze.controllers.auth;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;

public class LoginController {

    @FXML
    public VBox stack;
    @FXML
    public MFXButton connectButton;
    @FXML
    private MFXTextField email;
    @FXML
    private MFXTextField password;

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

        MFXProgressSpinner spinner = new MFXProgressSpinner();
        spinner.setRadius(18);
        stack.getChildren().remove(stack.getChildren().size() - 2);
        stack.getChildren().add(stack.getChildren().size() - 1, spinner);

        CompletableFuture.runAsync(() -> {
            if (AuthService.getInstance().authenticate(enteredEmail, enteredPassword)) {
                Platform.runLater(() -> {
                    LayoutManager.setLayout("borrow/index.fxml", "login success");
                    LayoutManager.addNavBar();
                });
                return;
            }

            Platform.runLater(() -> {
                stack.getChildren().remove(stack.getChildren().size() - 2);
                stack.getChildren().add(stack.getChildren().size() - 1, connectButton);
                LayoutManager.alert("Erreur lors de l'authentification");
            });
        });
    }

    @FXML
    private void register() {
        LayoutManager.setLayout("auth/register.fxml", "Register");
    }

}
