package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public final class UserDAO extends DAO<User> {
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private static UserDAO instance;

    public UserDAO(SessionFactory sf) {
        super(User.class, sf);
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }

        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();

        instance = this;
    }

    public void transferFunds(User from, User to, int amount) {
        em.getTransaction().begin();
        from.setFlow(from.getFlow() - amount);
        to.setFlow(to.getFlow() + amount);
        em.merge(from);
        em.merge(to);
        em.getTransaction().commit();
    }
    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public List<User> getAllAdmins() {
        return em.createQuery("SELECT u FROM User u WHERE u.admin = true", User.class).getResultList();
    }

    public List<User> getAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User createUser(String firstName, String lastName, String email, String password, boolean sleeping, boolean admin) {
        email = email.toLowerCase();

        em.getTransaction().begin();
        User user = new User(firstName, lastName, email, password, sleeping, admin);
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    public User updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        return user;
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User getUserByEmail(String email) {
        email = email.toLowerCase();
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream().findFirst().orElse(null);
    }

    public boolean isExist(String email) {
        return em.createQuery("SELECT COUNT(*) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult() > 0;
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

    public void refresh(User user) {
        em.getTransaction().begin();
        em.refresh(user);
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