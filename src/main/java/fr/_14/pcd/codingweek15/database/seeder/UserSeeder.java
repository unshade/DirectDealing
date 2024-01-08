package fr._14.pcd.codingweek15.database.seeder;

import fr._14.pcd.codingweek15.dao.UserDAO;

public class UserSeeder {

    public void seed() {

        UserDAO userDAO = UserDAO.getInstance();

        // Create users
        userDAO.createUser("John", "Doe", "john.doe@telecomnancy.eu", "password", 0, false, true);
        userDAO.createUser("Jane", "Doe", "john.doe@telecomnancy.eu", "password", 0, false, false);
        userDAO.createUser("Michele", "Doe", "michele.doe@telecomnancy.eu", "password", 0, false, false);

    }
}