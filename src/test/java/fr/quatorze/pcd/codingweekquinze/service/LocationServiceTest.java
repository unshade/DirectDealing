package fr.quatorze.pcd.codingweekquinze.service;

import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class LocationServiceTest {

    @Test
    @Order(1)
    void initDatabase() {
        assertDoesNotThrow(() -> LocationService.getInstance().initDatabase(HibernateUtil.getSessionFactory().openSession()));
    }

    @Test
    @Order(2)
    void getCitiesStartingWith() {
        List<String> exampleCities = List.of("Épernay", "Epernay", "ePernAY");
        for (String exampleCity : exampleCities) {
            var cities = LocationService.getInstance().getCitiesStartingWith(exampleCity);

            assertTrue(List.of("Épernay", "Épernay-sous-Gevrey").containsAll(cities));
        }
    }

    @Test
    @Order(3)
    void getCitiesNear() {
        String exampleCity = "Épernay";
        var cities = LocationService.getInstance().getCitiesNear(exampleCity, (float) 0.1);

        assertEquals(1, cities.size());

        var cities2 = LocationService.getInstance().getCitiesNear(exampleCity, (float) 3);

        assertTrue(cities2.containsAll(List.of("Épernay", "Mardeuil", "Pierry", "Moussy")));
    }
}