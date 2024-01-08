package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.Element;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public final class ElementDAO extends DAO<Element> {
    private static ElementDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    private ElementDAO(SessionFactory sf) {
        super(Element.class, sf);
        emf = Persistence.createEntityManagerFactory("michele");
        em = emf.createEntityManager();
        instance = this;
    }

    public static ElementDAO getInstance() {
        if (instance == null) {
            instance = new ElementDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public void createElement(String name, Integer price, String description, User owner) {
        em.getTransaction().begin();
        Element element = new Element(name, price, description, owner);
        em.persist(element);
        em.getTransaction().commit();
    }

    public List<Element> getAllElements() {
        return em.createQuery("SELECT u FROM Element u", Element.class).getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Element").executeUpdate();
        em.getTransaction().commit();
    }

    @Override
    public List<Element> search(Element criteria) {
        return null;
    }
}