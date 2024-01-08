package fr._14.pcd.codingweek15;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.layout.LayoutManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LayoutManager.init(stage);
        LayoutManager.setLayout("register.fxml", "Register");
    }

    public static void main(String[] args) {
//        UserDAO userDAO = UserDAO.getInstance();
//        ElementDAO elementDAO = ElementDAO.getInstance();
//        LoanDAO loanDAO = LoanDAO.getInstance();
//        userDAO.createUser("John", "Doe", "email", "password", 0, false, false);
//        userDAO.createUser("Jane", "Doe", "email", "password", 0, false, false);
//
//        userDAO.getAllUsers().forEach(user -> System.out.println(user.getFirstName()));
//
//        elementDAO.createElement("test", 0, "test", userDAO.getAllUsers().get(0));
//
//
//        loanDAO.createLoan(new Date(), new Date(), elementDAO.getAllElements().get(0), userDAO.getAllUsers().get(1));
//
//        loanDAO.getAllLoans().forEach(loan -> System.out.println(loan.getStartDate()));

        launch();
    }
}