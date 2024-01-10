package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.model.Availability;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public final class AvailabilityDAO extends DAO<Availability> {
    private static AvailabilityDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public AvailabilityDAO(SessionFactory sf) {
        super(Availability.class, sf);
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }

        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();

        instance = this;
    }

    public static AvailabilityDAO getInstance() {
        if (instance == null) {
            instance = new AvailabilityDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public Availability createAvailability(Element element, LocalDate fromDate, LocalDate toDate, ChronoUnit chronoUnit, Integer period) {
        em.getTransaction().begin();
        Availability availability = new Availability(element, fromDate, toDate, chronoUnit, period);
        em.persist(availability);
        em.getTransaction().commit();

        ElementDAO.getInstance().refresh(element);

        return availability;
    }

    public List<Availability> getAllAvailabilities() {
        return em.createQuery("SELECT u FROM Availability u", Availability.class)
                .getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Availability").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public List<Availability> search(Availability criteria) {
        return null;
    }
}