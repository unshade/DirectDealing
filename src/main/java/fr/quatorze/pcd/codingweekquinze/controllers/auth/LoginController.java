package fr.quatorze.pcd.codingweekquinze.controllers.auth;

import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController {

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
