package fr.quatorze.pcd.codingweekquinze.database.seeder;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.element.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;


public class LoanSeeder {

    public void seed() {

        LoanDAO loanDAO = LoanDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        ElementDAO elementDAO = ElementDAO.getInstance();

        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();
        ArrayList<Element> elements = (ArrayList<Element>) elementDAO.getAllElements();

        // Create loans with differents dates and users and elements
        loanDAO.createLoan(Date.valueOf(LocalDate.of(2024, 12, 12)), Date.valueOf(LocalDate.of(2024, 12, 12)), elements.get(0), users.get(1));
        loanDAO.createLoan(Date.valueOf(LocalDate.of(2024, 1, 11)), Date.valueOf(LocalDate.of(2024, 1, 11)), elements.get(1), users.get(2));
        loanDAO.createLoan(Date.valueOf(LocalDate.of(2024, 2, 10)), Date.valueOf(LocalDate.of(2024, 2, 10)), elements.get(2), users.get(0));
        loanDAO.createLoan(Date.valueOf(LocalDate.of(2024, 3, 9)), Date.valueOf(LocalDate.of(2024, 4, 9)), elements.get(3), users.get(2));
        loanDAO.createLoan(Date.valueOf(LocalDate.of(2024, 4, 8)), Date.valueOf(LocalDate.of(2024, 5, 8)), elements.get(4), users.get(0));

        // Add ratings
        loanDAO.addRating(loanDAO.getAllLoans().get(0), 5);
        loanDAO.addRating(loanDAO.getAllLoans().get(1), 4);
        loanDAO.addRating(loanDAO.getAllLoans().get(2), 3);
        loanDAO.addRating(loanDAO.getAllLoans().get(3), 2);
        loanDAO.addRating(loanDAO.getAllLoans().get(4), 1);

    }

}
