package fr.quatorze.pcd.codingweekquinze.database.seeder;

import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import javafx.fxml.FXML;

import java.util.ArrayList;

public class AvailabilitySeeder {

    public void seed() {

        ArrayList<Element> elements = (ArrayList<Element>) ElementDAO.getInstance().getAllElements();

        AvailabilityDAO availabilityDAO = AvailabilityDAO.getInstance();

        // Create availabilities
        availabilityDAO.createAvailability(elements.get(0), java.time.LocalDateTime.of(2024, 12, 12, 12, 12), java.time.LocalDateTime.of(2024, 12, 12, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(1), java.time.LocalDateTime.of(2024, 1, 11, 12, 12), java.time.LocalDateTime.of(2024, 1, 11, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(2), java.time.LocalDateTime.of(2024, 2, 10, 12, 12), java.time.LocalDateTime.of(2024, 2, 10, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(3), java.time.LocalDateTime.of(2024, 3, 9, 12, 12), java.time.LocalDateTime.of(2024, 4, 9, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(4), java.time.LocalDateTime.of(2024, 4, 8, 12, 12), java.time.LocalDateTime.of(2024, 5, 8, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(5), java.time.LocalDateTime.of(2024, 5, 7, 12, 12), java.time.LocalDateTime.of(2024, 6, 7, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(6), java.time.LocalDateTime.of(2024, 6, 6, 12, 12), java.time.LocalDateTime.of(2024, 7, 6, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(7), java.time.LocalDateTime.of(2024, 7, 5, 12, 12), java.time.LocalDateTime.of(2024, 8, 5, 12, 12), null, 0);
        availabilityDAO.createAvailability(elements.get(8), java.time.LocalDateTime.of(2024, 8, 4, 12, 12), java.time.LocalDateTime.of(2024, 9, 4, 12, 12), null, 0);



    }
}
