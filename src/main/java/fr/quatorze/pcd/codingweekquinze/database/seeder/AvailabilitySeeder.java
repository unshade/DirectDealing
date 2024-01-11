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
        availabilityDAO.createAvailability(elements.get(0), java.time.LocalDate.of(2024, 1, 3), java.time.LocalDate.of(2024, 1, 13), java.time.temporal.ChronoUnit.MONTHS, 2);
        availabilityDAO.createAvailability(elements.get(1), java.time.LocalDate.of(2024, 1, 12), java.time.LocalDate.of(2024, 1, 23), null, 0);
        availabilityDAO.createAvailability(elements.get(2), java.time.LocalDate.of(2024, 1, 12), java.time.LocalDate.of(2024, 1, 23), java.time.temporal.ChronoUnit.MONTHS, 1);
        availabilityDAO.createAvailability(elements.get(3), java.time.LocalDate.of(2024, 1, 13), java.time.LocalDate.of(2024, 1, 23), null, 0);
        availabilityDAO.createAvailability(elements.get(4), java.time.LocalDate.of(2024, 1, 2), java.time.LocalDate.of(2024, 1, 13), java.time.temporal.ChronoUnit.MONTHS, 1);
        availabilityDAO.createAvailability(elements.get(5), java.time.LocalDate.of(2024, 1, 18), java.time.LocalDate.of(2024, 1, 23), null, 0);
        availabilityDAO.createAvailability(elements.get(6), java.time.LocalDate.of(2024, 1, 15), java.time.LocalDate.of(2024, 1, 19), java.time.temporal.ChronoUnit.MONTHS, 1);
        availabilityDAO.createAvailability(elements.get(7), java.time.LocalDate.of(2024, 1, 14), java.time.LocalDate.of(2024, 1, 23), null, 0);
        availabilityDAO.createAvailability(elements.get(8), java.time.LocalDate.of(2024, 1, 12), java.time.LocalDate.of(2024, 1, 17), java.time.temporal.ChronoUnit.MONTHS, 1);



    }
}
