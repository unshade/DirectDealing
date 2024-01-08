package fr._14.pcd.codingweek15.database;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.dao.LoanDAO;
import fr._14.pcd.codingweek15.dao.MessageDAO;
import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.database.seeder.ElementSeeder;
import fr._14.pcd.codingweek15.database.seeder.LoanSeeder;
import fr._14.pcd.codingweek15.database.seeder.MessageSeeder;
import fr._14.pcd.codingweek15.database.seeder.UserSeeder;

public class Seeder {

    public static void main(String[] args) {

        // Drop all tables
        UserDAO.getInstance().dropTable();
        LoanDAO.getInstance().dropTable();
        ElementDAO.getInstance().dropTable();
        MessageDAO.getInstance().dropTable();

        // Create users
        UserSeeder userSeeder = new UserSeeder();
        userSeeder.seed();

        // Create elements
        ElementSeeder elementSeeder = new ElementSeeder();
        elementSeeder.seed();

        LoanSeeder loanSeeder = new LoanSeeder();
        loanSeeder.seed();

        MessageSeeder messageSeeder = new MessageSeeder();
        messageSeeder.seed();
    }
}
