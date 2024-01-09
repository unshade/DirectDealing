package fr._14.pcd.codingweek15.database.seeder;

import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.service.AuthService;

public class UserSeeder {

    public void seed() {

        UserDAO userDAO = UserDAO.getInstance();

        String pass = AuthService.getInstance().getEncoder().encode("password");

        // Create users
        userDAO.createUser("John", "Doe", "john.doe@telecomnancy.eu", pass, 0, false, true);
        userDAO.createUser("Jane", "Doe", "john.doe@telecomnancy.eu", pass, 0, false, false);
        userDAO.createUser("Michele", "Doe", "michele.doe@telecomnancy.eu", pass, 0, false, false);

    }
}