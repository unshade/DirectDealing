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

    public Availability createAvailability(Availability availability) {
        em.getTransaction().begin();
        em.persist(availability);
        em.getTransaction().commit();

        ElementDAO.getInstance().refresh(availability.getElement());

        return availability;
    }

    public List<Availability> getAllAvailabilities() {
        return em.createQuery("SELECT u FROM Availability u", Availability.class)
                .getResultList();
    }

    public List<Availability> getAvailabilitiesByElement(Element element) {
        return em.createQuery("SELECT u FROM Availability u WHERE u.element = :element", Availability.class)
                .setParameter("element", element)
                .getResultList();
    }

    public void deleteAvailability(Availability availability) {
        em.getTransaction().begin();
        Availability managedAvailability = em.merge(availability);
        em.remove(managedAvailability);
        em.getTransaction().commit();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Availability").executeUpdate();
        em.getTransaction().commit();
    }

    public Availability updateAvailability(Availability availability) {
        em.getTransaction().begin();
        em.merge(availability);
        em.getTransaction().commit();
        return availability;
    }

    @Override
    public List<Availability> search(Availability criteria) {
        return null;
    }
}