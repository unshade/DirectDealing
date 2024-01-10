package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.enums.LoanStatus;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.Notification;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public final class NotificationDAO extends DAO<Notification> {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private static NotificationDAO instance;

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

    public List<Notification> getNotifications(User user) {
        return em.createQuery("SELECT n FROM Notification n WHERE n.user = :user", Notification.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Override
    public List<Notification> search(Notification criteria) {
        return null;
    }
}