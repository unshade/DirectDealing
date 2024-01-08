package fr._14.pcd.codingweek15.layout;

import fr._14.pcd.codingweek15.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public final class LayoutManager {

    private static final String baseTitle = "Je suis une application";

    private static Stage stage;
    private static Pane pane;
    private static String currentLayout;

    public static void init(Stage stage) throws IOException {
        LayoutManager.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(pane = fxmlLoader.load());
        stage.setTitle(baseTitle);
        stage.setScene(scene);
        stage.show();
    }

    public static void setLayout(String layout, String title, Object... payload) {
        currentLayout = layout;

        FXMLLoader loader = new FXMLLoader(LayoutManager.class.getResource(layout));
        loader.setControllerFactory(clazz -> {
            try {
                return clazz.getConstructor(Object[].class).newInstance(payload);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            Parent scene = loader.load();

            pane.getChildren().clear();
            pane.getChildren().add(scene);

            stage.setTitle(title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
