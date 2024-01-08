package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.Message;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageDAOTest {

    @BeforeAll
    static void setUp() {
        new MessageDAO(HibernateUtil.getSessionFactory());
    }

    @Test
    void saveMessage() {
        User sender = new User("John", "Doe", "john.doe@gmail.com", "");
        User receiver = new User("Jane", "Doe", "jane.doe@gmail.com", "");
        Loan loan = new Loan();
        Message message = new Message(sender, receiver, loan, "Hello world");

        MessageDAO.getInstance().saveMessage(message);

        assertEquals(1, MessageDAO.getInstance().search(message).size());

        Message fetchedMessage = MessageDAO.getInstance().getById(message.getId());

        assertEquals(message, fetchedMessage);
    }

    @Test
    void search() {
    }

    @Test
    void getInstance() {
    }
}