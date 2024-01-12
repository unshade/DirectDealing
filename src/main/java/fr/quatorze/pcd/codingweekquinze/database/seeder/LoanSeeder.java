package fr.quatorze.pcd.codingweekquinze.database.seeder;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class LoanSeeder {

    public void seed() {

        LoanDAO loanDAO = LoanDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        ElementDAO elementDAO = ElementDAO.getInstance();

        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();
        ArrayList<Element> elements = (ArrayList<Element>) elementDAO.getAllElements();

        // Create loans with differents dates and users and elements
        loanDAO.createLoan(LocalDateTime.of(2024, 11, 20, 12, 12), LocalDateTime.of(2024, 11, 25, 12, 12), elements.get(0), users.get(1));
        loanDAO.createLoan(LocalDateTime.of(2024, 6, 5, 12, 12), LocalDateTime.of(2024, 6, 15, 12, 12), elements.get(6), users.get(2));
        loanDAO.createLoan(LocalDateTime.of(2024, 11, 10, 12, 12), LocalDateTime.of(2024, 11, 20, 12, 12), elements.get(11), users.get(0));


        // Add ratings
        loanDAO.addRating(loanDAO.getAllLoans().get(0), 5);
        loanDAO.addRating(loanDAO.getAllLoans().get(1), 4);
        loanDAO.addRating(loanDAO.getAllLoans().get(2), 3);
    }

}
