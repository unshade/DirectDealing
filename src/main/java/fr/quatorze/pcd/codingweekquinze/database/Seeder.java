package fr.quatorze.pcd.codingweekquinze.database;

import fr.quatorze.pcd.codingweekquinze.dao.*;
import fr.quatorze.pcd.codingweekquinze.database.seeder.*;

import java.util.logging.Level;

public class Seeder {

    public static void main(String[] args) {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("org.slf4j").setLevel(Level.OFF);

        // Drop all tables
        UserDAO.getInstance().dropTable();
        LoanDAO.getInstance().dropTable();
        ElementDAO.getInstance().dropTable();
        MessageDAO.getInstance().dropTable();
        NotificationDAO.getInstance().dropTable();

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

        // Create notifications
        NotificationSeeder notificationSeeder = new NotificationSeeder();
        notificationSeeder.seed();
    }
}
