package fr.quatorze.pcd.codingweekquinze.model;

import fr.quatorze.pcd.codingweekquinze.dao.AvailabilityDAO;
import fr.quatorze.pcd.codingweekquinze.dao.ElementDAO;
import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Order(3)
public class AvailabilityTest {

    @Test
    void testAvailability() {
        User sender = UserDAO.getInstance().getUserByName("John", "Doe");
        User receiver = UserDAO.getInstance().getUserByName("Jane", "Doe");
        Element element = ElementDAO.getInstance().getElementsByOwner(sender).get(0);
        Loan loan = LoanDAO.getInstance().createLoan(LocalDateTime.now(), LocalDateTime.now(), element, sender);

        // Check if the element knows the loan
        Assertions.assertTrue(element.getLoans().contains(loan));

        // Check if the loan has the right element
        Assertions.assertEquals(loan.getItem(), element);

        Availability a1 = AvailabilityDAO.getInstance().createAvailability(element, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, 0);

        Assertions.assertEquals(1, AvailabilityDAO.getInstance().getAllAvailabilities().size());
        Assertions.assertEquals(a1.getElement(), element);
        Assertions.assertTrue(element.getAvailabilities().contains(a1));

        System.out.println(element.getName());
        for (Availability a : element.getAvailabilities()) {
            System.out.println(a.getFromDate());
            System.out.println(a.getToDate());
        }

        System.out.println("Loans:");

        for (Loan l : element.getLoans()) {
            System.out.println(l.getStartDate());
            System.out.println(l.getEndDate());
        }

        // Check overlaps
        Assertions.assertTrue(element.isAvailable(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2)));
        Assertions.assertFalse(element.isAvailable(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusDays(2)));

        // Sleep
        sender.setSleeping(true);
        Assertions.assertFalse(element.isAvailable(LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusDays(2)));
    }
}
