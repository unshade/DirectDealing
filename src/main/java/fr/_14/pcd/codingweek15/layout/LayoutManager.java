package fr._14.pcd.codingweek15.layout;

import fr._14.pcd.codingweek15.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public final class LayoutManager {

    private static String baseTitle = "Load Layout";

    private static Stage stage;
    private static Pane pane;
    private static String currentLayout;

    public static void init(Stage stage) throws IOException {
        if (LayoutManager.stage != null) {
            throw new IllegalStateException("LayoutManager already initialized");
        }

        LayoutManager.stage = stage;
        currentLayout = "main-layout.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(currentLayout));
        Scene scene = new Scene(pane = fxmlLoader.load());
        stage.setTitle(baseTitle);
        stage.setScene(scene);
        stage.show();
    }

    public static void alert(String message) {
        System.out.println("Alert: " + message);
    }

    public static void setLayout(String layout, String title, Object... payload) {
        currentLayout = layout;
        baseTitle = title;
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource(layout));
        loader.setControllerFactory(clazz -> {
            try {
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

            pane.getChildren().clear();
            pane.getChildren().add(scene);

            stage.setTitle(baseTitle);
        } catch (IOException e) {
            System.err.println("Error caca loading layout " + layout);
            throw new RuntimeException(e);
        }
    }
}
