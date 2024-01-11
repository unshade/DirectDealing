package fr.quatorze.pcd.codingweekquinze.database.seeder;

import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ElementSeeder {

    public void seed() {

        ElementDAO elementDAO = ElementDAO.getInstance();

        UserDAO userDAO = UserDAO.getInstance();
        ArrayList<User> users = (ArrayList<User>) userDAO.getAllUsers();

        // Create elements
        elementDAO.createElement("Bike", 30, "A bike", users.get(0), null);
        elementDAO.createElement("Car", 25, "A car", users.get(1), null);
        Element test = elementDAO.createElement("Motorbike", 5, "A motorbike", users.get(2), null);
        elementDAO.createElement("Scooter", 10, "A scooter", users.get(0), null);
        elementDAO.createElement("Skateboard", 40, "A skateboard", users.get(1), null);
        Element test2 = elementDAO.createElement("Roller", 30, "A roller", users.get(2), null);
        elementDAO.createElement("Ski", 80, "A ski", users.get(0), null);
        elementDAO.createElement("Snowboard", 100, "A snowboard", users.get(1), null);
        elementDAO.createElement("Surf", 68, "A surf", users.get(2), null);

        AvailabilityDAO.getInstance().createAvailability(test, LocalDate.of(2024, 1, 12), LocalDate.of(2024, 1, 13), ChronoUnit.MONTHS, 1);
        AvailabilityDAO.getInstance().createAvailability(test2, LocalDate.of(2024, 1, 23), LocalDate.of(2024, 1, 23), null, null);
    }
}