package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.Message;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public final class MessageDAO extends DAO<Message> {

    private static MessageDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public MessageDAO(SessionFactory sf) {
        super(sf);
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }

        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();

        instance = this;
    }

    public void saveMessage(Message message) {
        em.getTransaction().begin();
        em.persist(message);
        em.getTransaction().commit();
    }

    @Override
    public List<Message> search(Message criteria) {
        User sender = criteria.getSender();
        User receiver = criteria.getReceiver();
        Loan loan = criteria.getLoan();
        return em.createQuery("SELECT m FROM Message m WHERE m.sender = :sender AND m.receiver = :receiver AND m.loan = :loan", Message.class)
                .setParameter("sender", sender)
                .setParameter("receiver", receiver)
                .setParameter("loan", loan)
                .getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Message").executeUpdate();
        em.getTransaction().commit();
    }

    public void createMessage(String content, User sender, User receiver, Loan loan) {
        em.getTransaction().begin();
        Message message = new Message(sender, receiver, loan, content);
        em.persist(message);
        em.getTransaction().commit();
    }

    public static MessageDAO getInstance() {
        if (instance == null) {
            instance = new MessageDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }
}