package fr.quatorze.pcd.codingweekquinze.layout;

import fr.quatorze.pcd.codingweekquinze.MainApplication;
import fr.quatorze.pcd.codingweekquinze.controllers.NavBarController;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.util.FXMLLoaderUtil;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
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


        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA) // Optional if you don't need JavaFX's default theme, still recommended though
                .themes(MaterialFXStylesheets.forAssemble(true)) // Adds the MaterialFX's default theme. The boolean argument is to include legacy controls
                .setDeploy(true) // Whether to deploy each theme's assets on a temporary dir on the disk
                .setResolveAssets(true) // Whether to try resolving @import statements and resources urls
                .build() // Assembles all the added themes into a single CSSFragment (very powerful class check its documentation)
                .setGlobal(); // Finally, sets the produced stylesheet as the global User-Agent stylesheet

        LayoutManager.stage = stage;
        currentLayout = "main-layout.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(currentLayout));
        Scene scene = new Scene(pane = fxmlLoader.load());

        stage.setTitle(baseTitle);
        stage.setScene(scene);
        stage.show();
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void success(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                            Platform.runLater(() -> LayoutManager.setLayout("auth/login.fxml", "Loan View"));
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

}
