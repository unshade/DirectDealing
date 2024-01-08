package fr._14.pcd.codingweek15;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr._14.pcd.codingweek15.controller.UserController;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.model.User;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
    }

    public static void main(String[] args) throws Exception {

        String databaseUrl = "jdbc:sqlite:sample.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

        // Création des tables
        TableUtils.createTableIfNotExists(connectionSource, User.class);

        // Utilisation du contrôleur
        UserController userController = new UserController(databaseUrl);
        userController.createUser("Alice");

        launch();
    }
}