package fr._14.pcd.codingweek15.database.seeder;

import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.dao.MessageDAO;
import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.User;

import java.util.List;

public class MessageSeeder {

    public void seed() {

        UserDAO userDAO = UserDAO.getInstance();
        LoanDAO loanDAO = LoanDAO.getInstance();
        MessageDAO messageDAO = MessageDAO.getInstance();

        List<User> users = userDAO.getAllUsers();
        List<Loan> loans = loanDAO.getAllLoans();

        // Create messages
        messageDAO.createMessage("Hello bro", users.get(1), users.get(0), loans.get(0));
        messageDAO.createMessage("Hi bro", users.get(0), users.get(1), loans.get(0));
        messageDAO.createMessage("Hello bro", users.get(2), users.get(1), loans.get(1));
        messageDAO.createMessage("Hi bro", users.get(1), users.get(2), loans.get(1));
        messageDAO.createMessage("Hello bro", users.get(0), users.get(2), loans.get(2));
        messageDAO.createMessage("Hi bro", users.get(2), users.get(0), loans.get(2));
        messageDAO.createMessage("Hello bro", users.get(2), users.get(0), loans.get(3));
        messageDAO.createMessage("Hi bro", users.get(0), users.get(2), loans.get(3));
        messageDAO.createMessage("Hello bro", users.get(0), users.get(1), loans.get(4));
        messageDAO.createMessage("Hi bro", users.get(1), users.get(0), loans.get(4));
    }

}
