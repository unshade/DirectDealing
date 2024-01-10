package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.model.Notification;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public final class NotificationDAO extends DAO<Notification> {

    private static NotificationDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;


    private NotificationDAO(SessionFactory sf) {
        super(Notification.class, sf);
        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();
        instance = this;
    }

    public static NotificationDAO getInstance() {
        if (instance == null) {
            instance = new NotificationDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createNativeQuery("DROP TABLE IF EXISTS Notification").executeUpdate();
        em.getTransaction().commit();
    }

    public void markAsRead(Notification notification) {
        em.getTransaction().begin();
        notification.setRead(true);
        em.merge(notification);
        em.getTransaction().commit();
    }

    public void createNotification(User user, String message) {
        Notification notification = new Notification(user, message);
        em.getTransaction().begin();
        em.persist(notification);
        em.getTransaction().commit();
    }

    public List<Notification> getNotifications(User user) {
        em.getTransaction().begin();
        List<Notification> notifications = em.createQuery("SELECT n FROM Notification n WHERE n.user = :user", Notification.class)
                .setParameter("user", user)
                .getResultList();
        em.getTransaction().commit();
        return notifications;
    }

    @Override
    public List<Notification> search(Notification criteria) {
        return null;
    }
}