package fr._14.pcd.codingweek15.database.seeder;

import fr._14.pcd.codingweek15.dao.ElementDAO;
import fr._14.pcd.codingweek15.dao.UserDAO;
import fr._14.pcd.codingweek15.model.User;

import java.util.ArrayList;

public class ElementSeeder {

     public void seed() {

          ElementDAO elementDAO = ElementDAO.getInstance();

          UserDAO userDAO = UserDAO.getInstance();
          ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();

          // Create elements
          elementDAO.createElement("Bike", 500, "A bike", users.get(0));
          elementDAO.createElement("Car", 25000, "A car", users.get(1));
          elementDAO.createElement("Motorbike", 5000, "A motorbike", users.get(2));
          elementDAO.createElement("Scooter", 3000, "A scooter", users.get(0));
          elementDAO.createElement("Skateboard", 40, "A skateboard", users.get(1));
          elementDAO.createElement("Roller", 30, "A roller", users.get(2));
          elementDAO.createElement("Ski", 80, "A ski", users.get(0));
          elementDAO.createElement("Snowboard", 100, "A snowboard", users.get(1));
          elementDAO.createElement("Surf", 68, "A surf", users.get(2));
     }
}