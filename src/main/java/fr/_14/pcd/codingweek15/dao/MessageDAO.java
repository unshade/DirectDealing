package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.Message;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public final class MessageDAO extends DAO<Message> {

    private static MessageDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public MessageDAO(SessionFactory sf) {
        super(Message.class, sf);
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }

        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();

        instance = this;
    }

    public void saveMessages(Message... message) {
        em.getTransaction().begin();
        for (Message m : message) {
            em.persist(m);
        }
        em.getTransaction().commit();
    }

    @Override
    public List<Message> search(Message criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cr = cb.createQuery(Message.class);
        Root<Message> root = cr.from(Message.class);
        cr.select(root);

        System.out.println("criteria: ");
        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }
        if (criteria.getContent() != null) {
            predicates.add(cb.equal(root.get("content"), criteria.getContent()));
        }
        if (criteria.getDate() != null) {
            predicates.add(cb.equal(root.get("date"), criteria.getDate()));
        }
        if (criteria.getLoan() != null) {
            predicates.add(cb.equal(root.get("loan"), criteria.getLoan()));
        }
        if (criteria.getSender() != null) {
            predicates.add(cb.equal(root.get("sender"), criteria.getSender()));
        }
        if (criteria.getReceiver() != null) {
            predicates.add(cb.equal(root.get("receiver"), criteria.getReceiver()));
        }
        cr.where(predicates.toArray(new Predicate[0]));
        Query query = em.createQuery(cr);
        //noinspection unchecked
        return query.getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Message").executeUpdate();
        em.getTransaction().commit();
    }

    public Message createMessage(String content, User sender, User receiver, Loan loan) {
        em.getTransaction().begin();
        Message message = new Message(sender, receiver, loan, content);
        em.persist(message);
        em.getTransaction().commit();
        return message;
    }

    public static MessageDAO getInstance() {
        if (instance == null) {
            instance = new MessageDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }
}