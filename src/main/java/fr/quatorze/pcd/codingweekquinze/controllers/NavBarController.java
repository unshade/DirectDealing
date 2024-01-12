package fr.quatorze.pcd.codingweekquinze.controllers;

import fr.quatorze.pcd.codingweekquinze.MainApplication;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.layout.component.NotificationButton;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import io.github.palexdev.materialfx.utils.ToggleButtonsUtil;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class NavBarController implements Observer {

    @FXML
    private VBox navbar;
    @FXML
    private StackPane logoContainer;

    @FXML
    private Label wallet;

    @FXML
    private HBox walletBox;

    private NotificationButton notificationButton;
    private MFXIconWrapper wrapper;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    public NavBarController() {
        AuthService.getInstance().getCurrentUser().addObserver(this);
        ToggleButtonsUtil.addAlwaysOneSelectedSupport(toggleGroup);
    }

    @FXML
    private void initialize() {

        if (AuthService.getInstance().getCurrentUser() == null) {
            walletBox.setVisible(false);
        } else {
            wallet.setText(AuthService.getInstance().getCurrentUser().getFlow() + "⚘");
        }

        List<Node> toggleButtons = new ArrayList<>();
        ToggleButton t2 = createToggle("fas-shop", "Emprunts");
        t2.setOnAction(actionEvent -> LayoutManager.setLayout("borrow/index.fxml", "Emprunt"));
        t2.setSelected(true);
        toggleButtons.add(t2);
        ToggleButton t3 = createToggle("fas-shop", "Prêts");
        t3.setOnAction(actionEvent -> LayoutManager.setLayout("loan/index.fxml", "Prêt"));
        toggleButtons.add(t3);
        ToggleButton t4 = createToggle("fas-calendar-days", "Planning");
        t4.setOnAction(actionEvent -> LayoutManager.setLayout("planning.fxml", "Planning"));
        toggleButtons.add(t4);
        ToggleButton t5 = createToggle("fas-user", "Mon compte");
        t5.setOnAction(actionEvent -> LayoutManager.setLayout("account.fxml", "Mon compte"));
        toggleButtons.add(t5);
        MFXButton t6 = createNotificationButton("fas-bell");
        toggleButtons.add(t6);
        MFXButton t7 = createButton("", "Déconnexion");
        t7.setOnAction(actionEvent -> logout());
        //toggleButtons.add(t7);

        navbar.getChildren().addAll(0, toggleButtons);

        // The only way to get a fucking smooth image in this shitty framework
        Image image = new Image(MainApplication.load("logo_alt.png"), 64, 64, true, true);
        ImageView logo = new ImageView(image);
        logoContainer.getChildren().add(logo);

        navbar.getChildren().add(t7);
    }

    @FXML
    private void logout() {
        AuthService.getInstance().endSession();
        LayoutManager.removeNavBar();
        LayoutManager.setLayout("auth/login.fxml", "Se connecter");
    }


    @Override
    public void update() {
        wallet.setText(AuthService.getInstance().getCurrentUser().getFlow() + "⚘");

        boolean hasNotification = AuthService.getInstance().getCurrentUser().getNotifications().stream().anyMatch(notification -> !notification.isRead());

        if (!hasNotification) {
            notificationButton.setDisable(true);
        }

        ((MFXFontIcon) wrapper.getIcon()).setColor(hasNotification ? Color.WHITE: Color.SLATEGRAY);
    }

    private ToggleButton createToggle(String icon, String text) {
        return createToggle(icon, text, 0);
    }

    private NotificationButton createNotificationButton(String icon) {
        boolean hasNotification = AuthService.getInstance().getCurrentUser().getNotifications().stream().anyMatch(notification -> !notification.isRead());

        wrapper = new MFXIconWrapper(icon, 24, hasNotification ? Color.WHITE : Color.SLATEGRAY, 32);
        notificationButton = new NotificationButton(wrapper);
        notificationButton.setStyle("""
                -fx-background-color: transparent;
                	-fx-background-radius: 0;
                	-fx-border-color: transparent;
                	-fx-border-radius: 0;
                	-fx-pref-height: 40;

                	-fx-font-family: 'Open Sans SemiBold';
                	-fx-font-size: 13;
                	-fx-text-fill: white;
                                """);
        notificationButton.setAlignment(Pos.CENTER_LEFT);
        notificationButton.setMaxWidth(Double.MAX_VALUE);
        notificationButton.addObserver(this);

        if (!hasNotification) notificationButton.setDisable(true);

        return notificationButton;
    }

    private MFXButton createButton(String icon, String text) {
        return createButton(icon, text, 0);
    }

    private MFXButton createButton(String icon, String text, double rotate) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
        MFXButton button = new MFXButton(text, wrapper);
        button.setStyle("""
                -fx-background-color: transparent;
                	-fx-background-radius: 0;
                	-fx-border-color: transparent;
                	-fx-border-radius: 0;
                	-fx-pref-height: 40;

                	-fx-font-family: 'Open Sans SemiBold';
                	-fx-font-size: 13;
                	-fx-text-fill: white;
                                """);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setMaxWidth(Double.MAX_VALUE);
        if (rotate != 0) wrapper.getIcon().setRotate(rotate);
        return button;
    }

    private ToggleButton createToggle(String icon, String text, double rotate) {
        MFXIconWrapper wrapper = new MFXIconWrapper(icon, 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(text, wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(toggleGroup);
        if (rotate != 0) wrapper.getIcon().setRotate(rotate);
        return toggleNode;
    }
}
