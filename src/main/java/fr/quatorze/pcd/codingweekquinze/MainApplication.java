package fr.quatorze.pcd.codingweekquinze;

import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.layout.LayoutManager;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
        LayoutManager.setLayout("auth/login.fxml", "Loan View");
    }

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("org.slf4j").setLevel(Level.OFF);

        Locale.setDefault(Locale.FRANCE);
        HibernateUtil.getSessionFactory();
        UserDAO.getInstance();
        launch();
    }
}