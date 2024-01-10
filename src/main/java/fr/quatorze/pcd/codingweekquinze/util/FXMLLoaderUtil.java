package fr.quatorze.pcd.codingweekquinze.util;

import fr.quatorze.pcd.codingweekquinze.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.function.Consumer;

public class FXMLLoaderUtil {

    public static void inject(String fxml, Object controller, Consumer<Node> callback) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
            fxmlLoader.setControllerFactory(c -> controller);
            Parent node = fxmlLoader.load();
            callback.accept(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
