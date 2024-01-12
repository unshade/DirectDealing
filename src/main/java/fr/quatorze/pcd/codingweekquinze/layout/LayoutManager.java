package fr.quatorze.pcd.codingweekquinze.layout;

import fr.quatorze.pcd.codingweekquinze.MainApplication;
import fr.quatorze.pcd.codingweekquinze.controllers.NavBarController;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXNotificationCenter;
import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.materialfx.notifications.base.INotification;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.mfxresources.fonts.IconDescriptor;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeSolid;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public final class LayoutManager {
    private static String baseTitle = "Load Layout";

    private static Stage stage;
    private static BorderPane pane;
    private static String currentLayout;

    public static void init(Stage stage) throws IOException {
        if (LayoutManager.stage != null) {
            throw new IllegalStateException("LayoutManager already initialized");
        }

        // CSSFX.start();

        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        LayoutManager.stage = stage;
        currentLayout = "main-layout.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(currentLayout));
        Scene scene = new Scene(pane = fxmlLoader.load());

        stage.setTitle(baseTitle);
        stage.setScene(scene);
        stage.show();


        Platform.runLater(() -> {
            MFXNotificationSystem.instance().initOwner(stage);
            MFXNotificationCenterSystem.instance().initOwner(stage);

            MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
            center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
                {
                    setPrefHeight(400);
                }
            });
        });
    }

    public static void removeNavBar() {
        pane.setRight(null);
    }

    public static void addNavBar() {
        FXMLLoaderUtil.inject("navbar.fxml", new NavBarController(), node -> {
            pane.setRight(node);
        });
    }

    public static void alert(String message) {
        System.out.println("Alert: " + message);
        MFXNotificationSystem.instance()
                .setPosition(NotificationPos.TOP_CENTER)
                .publish(createNotification(message, "Erreur", FontAwesomeSolid.CIRCLE_EXCLAMATION, "error"));
    }

    public static void success(String message) {
        MFXNotificationSystem.instance()
                .setPosition(NotificationPos.TOP_CENTER)
                .publish(createNotification(message, "Succès", FontAwesomeSolid.CIRCLE_CHECK, "success"));
    }


    public static void setLayout(String layout, String title, Object... payload) {
        currentLayout = layout;
        baseTitle = title;
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(layout));
        loader.setControllerFactory(clazz -> {
            try {
                if (clazz.isAnnotationPresent(RequiresAuth.class)) {
                    if (!AuthService.getInstance().isAuthenticated()) {
                        new Thread(() -> {
                            Platform.runLater(() -> LayoutManager.setLayout("auth/login.fxml", "Se connecter"));
                        }).start();
                        return null;
                    }
                }

                if (payload == null) {
                    return clazz.getConstructors()[0].newInstance();
                } else {
                    return clazz.getConstructors()[0].newInstance(payload);
                }
            } catch (Exception e) {
                System.err.println("Error while loading layout " + layout);
                throw new RuntimeException(e);
            }
        });

        try {
            Parent scene = loader.load();

            pane.setCenter(scene);

            stage.setTitle(baseTitle);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    private static INotification createNotification(String message, String header, IconDescriptor iconDescriptor, String bgClass) {
        CustomNotification notification = new CustomNotification(header, iconDescriptor, bgClass);
        notification.setContentText(message);
        return notification;
    }

    private static class CustomNotification extends MFXSimpleNotification {
        private final StringProperty headerText;
        private final StringProperty contentText = new SimpleStringProperty();

        public CustomNotification(String headerTxt, IconDescriptor desc, String bgClass) {
            this.headerText = new SimpleStringProperty(headerTxt);

            MFXFontIcon fi = new MFXFontIcon();
            fi.setDescription(desc.getDescription());
            fi.setSize(16);
            MFXIconWrapper icon = new MFXIconWrapper(fi, 32);
            Label headerLabel = new Label();
            headerLabel.textProperty().bind(headerText);
            HBox header = new HBox(10, icon, headerLabel);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(InsetsFactory.of(5, 0, 5, 0));
            header.setMaxWidth(Double.MAX_VALUE);


            Label contentLabel = new Label();
            contentLabel.getStyleClass().add("content");
            contentLabel.textProperty().bind(contentText);
            contentLabel.setWrapText(true);
            contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            contentLabel.setAlignment(Pos.TOP_LEFT);

            BorderPane container = new BorderPane();
            container.getStyleClass().add("notification_" + bgClass);
            container.getStyleClass().add("notification");
            container.setTop(header);
            container.setCenter(contentLabel);
            container.getStylesheets().add(MainApplication.load("css/LayoutNotification.css"));
            container.setMaxWidth(400);

            setContent(container);
        }

        public String getHeaderText() {
            return headerText.get();
        }

        public StringProperty headerTextProperty() {
            return headerText;
        }

        public void setHeaderText(String headerText) {
            this.headerText.set(headerText);
        }

        public String getContentText() {
            return contentText.get();
        }

        public StringProperty contentTextProperty() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText.set(contentText);
        }
    }
}
