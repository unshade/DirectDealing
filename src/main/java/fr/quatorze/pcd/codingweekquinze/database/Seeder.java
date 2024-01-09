package fr.quatorze.pcd.codingweekquinze.database;

import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.MessageDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.database.seeder.ElementSeeder;
import fr.quatorze.pcd.codingweekquinze.database.seeder.LoanSeeder;
import fr.quatorze.pcd.codingweekquinze.database.seeder.MessageSeeder;
import fr.quatorze.pcd.codingweekquinze.database.seeder.UserSeeder;

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
