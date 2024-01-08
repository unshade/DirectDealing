package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public final class UserDAO extends DAO<User> {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private static UserDAO instance;

    private UserDAO(SessionFactory sf) {
        super(sf);
        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();
        instance = this;
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public User createUser(String firstName, String lastName, String email, String password, int flow, boolean sleeping, boolean admin) {
        em.getTransaction().begin();
        User user = new User(firstName, lastName, email, password, flow, sleeping, admin);
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User getUserByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User getUserByName(String firstName, String lastName) {
        return em.createQuery("SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName", User.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM User").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public List<User> search(User criteria) {
        return em.createQuery("SELECT u FROM User u WHERE u.firstName LIKE :firstName AND u.lastName LIKE :lastName AND u.email LIKE :email", User.class)
                .setParameter("firstName", "%" + criteria.getFirstName() + "%")
                .setParameter("lastName", "%" + criteria.getLastName() + "%")
                .setParameter("email", "%" + criteria.getEmail() + "%")
                .getResultList();
    }
}