package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;

public class AvailabilityTest {

    @BeforeAll
    static void setUp() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        java.util.logging.Logger.getLogger("org.slf4j").setLevel(Level.OFF);

        // So that the database is recreated before each test
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        HibernateUtil.recreateSessionFactory(configuration);
    }

    @Test
    void testAvailability() {
        User sender = UserDAO.getInstance().createUser("John", "Doe", "", "", 0, false, false);
        User receiver = UserDAO.getInstance().createUser("Jane", "Doe", "", "", 0, false, false);
        Element element = ElementDAO.getInstance().createElement("Test", 0, "", sender);
        Loan loan = LoanDAO.getInstance().createLoan(new Date(), new Date(), element, receiver);

        // Check if the element knows the loan
        Assertions.assertTrue(element.getLoans().contains(loan));

        // Check if the loan has the right element
        Assertions.assertEquals(loan.getItem(), element);

        Availability a1 = AvailabilityDAO.getInstance().createAvailability(element, LocalDate.now(), LocalDate.now().plusDays(1), null, 0);

        Assertions.assertEquals(1, AvailabilityDAO.getInstance().getAllAvailabilities().size());
        Assertions.assertEquals(a1.getElement(), element);
        Assertions.assertTrue(element.getAvailabilities().contains(a1));

        // Check overlaps
        Assertions.assertTrue(element.isAvailable(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2)));
        Assertions.assertFalse(element.isAvailable(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusDays(2)));
    }
}
