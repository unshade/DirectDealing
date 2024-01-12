package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
class NotificationDAOTest {

    @Test
    @Order(1)
    void notifications() {
        User receiver = UserDAO.getInstance().getUserByName("Jane", "Doe");

        assertEquals(0, NotificationDAO.getInstance().getNotifications(receiver).size());

        NotificationDAO.getInstance().createNotification(receiver, "test");

        assertEquals(1, NotificationDAO.getInstance().getNotifications(receiver).size());
        assertEquals("test", NotificationDAO.getInstance().getNotifications(receiver).get(0).getMessage());
    }
}