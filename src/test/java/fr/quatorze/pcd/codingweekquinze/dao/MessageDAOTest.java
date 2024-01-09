package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.dao.LoanDAO;
import fr.quatorze.pcd.codingweekquinze.dao.MessageDAO;
import fr.quatorze.pcd.codingweekquinze.dao.UserDAO;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.Message;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageDAOTest {

    @BeforeAll
    static void setUp() {
        // So that the database is recreated before each test
        Configuration configuration = new Configuration();
        configuration.configure();
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        HibernateUtil.recreateSessionFactory(configuration);
        new MessageDAO(HibernateUtil.getSessionFactory());
    }

    @Test
    @Order(1)
    void saveMessage() {
        User sender = UserDAO.getInstance().createUser("John", "Doe", "", "", 0, false, false);
        User receiver = UserDAO.getInstance().createUser("Jane", "Doe", "", "", 0, false, false);
        Loan loan = LoanDAO.getInstance().createLoan(new Date(), new Date(), null, sender);
        Message message = MessageDAO.getInstance().createMessage("Hello", sender, receiver, loan);

        Message fetchedMessage = MessageDAO.getInstance().getById(message.getId());

        assertEquals(message, fetchedMessage);
    }

    @Test
    @Order(2)
    void search() {
        User sender = UserDAO.getInstance().createUser("John", "Doe", "", "", 0, false, false);
        User receiver = UserDAO.getInstance().createUser("Jane", "Doe", "", "", 0, false, false);
        Loan loan = LoanDAO.getInstance().createLoan(new Date(), new Date(), null, sender);
        Message message = MessageDAO.getInstance().createMessage("Hello", sender, receiver, loan);

        var result1 = MessageDAO.getInstance().search(message);
        assertEquals(1, result1.size());
        assertEquals(message, result1.get(0));

        var result2 = MessageDAO.getInstance().search(new Message(sender, receiver, loan, "Hello"));
        assertEquals(1, result2.size());
        assertEquals(message, result2.get(0));

        MessageDAO.getInstance().createMessage("Hello", sender, receiver, loan);
        var result3 = MessageDAO.getInstance().search(new Message(sender, receiver, loan, "Hello"));
        assertEquals(2, result3.size());

        MessageDAO.getInstance().createMessage("Not", sender, receiver, loan);
        var result4 = MessageDAO.getInstance().search(new Message(sender, receiver, loan, "Hello"));
        assertEquals(2, result4.size());

        assertEquals(1, MessageDAO.getInstance().search(new Message(sender, receiver, loan, "Not")).size());
        assertEquals(3, MessageDAO.getInstance().search(new Message(sender, receiver, loan, null)).size());
        assertEquals(4, MessageDAO.getInstance().search(new Message(null, null, null, null)).size());
    }
}