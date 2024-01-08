package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.User;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UserDAO extends DAO<User> {
  private EntityManagerFactory emf;
  private EntityManager em;

  public UserDAO(SessionFactory sf) {
    super(sf);
    emf = Persistence.createEntityManagerFactory("user");
    em = emf.createEntityManager();
  }

  public void createUser(String firstName, String lastName, String email, String password) {
    em.getTransaction().begin();
    User user = new User(firstName, lastName, email, password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setEmail(email);
    user.setPassword(password);
    em.persist(user);
    em.getTransaction().commit();
  }

  public List<User> getAllUsers() {
    return em.createQuery("SELECT u FROM User u", User.class).getResultList();
  }

  @Override
  public List<User> search(User criteria) {
    return null;
  }
}