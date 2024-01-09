package fr._14.pcd.codingweek15;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
        LayoutManager.setLayout("auth/login.fxml", "Loan View");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.FRANCE);

        HibernateUtil.getSessionFactory();
        UserDAO.getInstance();
        launch();
    }
}