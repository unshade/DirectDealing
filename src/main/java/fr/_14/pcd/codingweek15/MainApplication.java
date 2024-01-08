package fr._14.pcd.codingweek15;

import fr._14.pcd.codingweek15.layout.LayoutManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}