package fr._14.pcd.codingweek15;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
        LayoutManager.setLayout("select-loan-view.fxml", "Loan View", "test");
    }

    public static void main(String[] args) throws Exception {
        UserDAO userDAO = new UserDAO(HibernateUtil.getSessionFactory());
        userDAO.createUser("John", "Doe", "mail", "password");

        launch();
    }
}