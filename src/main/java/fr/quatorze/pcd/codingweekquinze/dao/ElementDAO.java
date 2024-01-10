package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.service.AuthService;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ElementDAO extends DAO<Element> {
    private static ElementDAO instance;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ElementDAO(SessionFactory sf) {
        super(Element.class, sf);
        if (instance != null) {
            throw new IllegalStateException("Already instantiated");
        }

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

    public List<Element> getAllElementExceptUser(User user) {
        return em.createQuery("SELECT u FROM Element u WHERE u.owner != :user", Element.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Element createElement(String name, Integer price, String description, User owner) {
        em.getTransaction().begin();
        Element element = new Element(name, price, description, owner, false);
        em.persist(element);
        em.getTransaction().commit();

        return element;
    }

    public Element createElement(String name, Integer price, String description, User owner, boolean isService) {
        em.getTransaction().begin();
        Element element = new Element(name, price, description, owner, isService);
        em.persist(element);
        em.getTransaction().commit();

        return element;
    }

    public List<Element> getAllElements() {
        return em.createQuery("SELECT u FROM Element u", Element.class).getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Element").executeUpdate();
        em.getTransaction().commit();
    }

    public List<Element> getElementsByOwner(User owner) {
        return em.createQuery("SELECT u FROM Element u WHERE u.owner = :owner", Element.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    public void refresh(Element element) {
        em.getTransaction().begin();
        em.refresh(element);
        em.getTransaction().commit();
    }

    @SuppressWarnings("unchecked")
    public List<Element> search(String name, Date startDate, Date endDate, Integer rating, String type, boolean isBorrowed) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Element> cr = cb.createQuery(Element.class);
        Root<Element> root = cr.from(Element.class);
        cr.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if(isBorrowed) {
            predicates.add(cb.notEqual(root.get("owner"), AuthService.getInstance().getCurrentUser()));
        } else {
            predicates.add(cb.equal(root.get("owner"), AuthService.getInstance().getCurrentUser()));
        }

        if (name != null) {
            predicates.add(cb.or(
                    cb.like(root.get("name"), "%" + name + "%"),
                    cb.like(root.get("description"), "%" + name + "%")
            ));
        }

        if (startDate != null && endDate != null) {
            // Sous-requête pour trouver les emprunts qui se chevauchent avec la période spécifiée
            Subquery<Loan> loanSubquery = cr.subquery(Loan.class);
            Root<Loan> loanRoot = loanSubquery.from(Loan.class);
            loanSubquery.select(loanRoot);

            // Condition pour vérifier le chevauchement
            Predicate loanOverlap = cb.and(
                    cb.lessThanOrEqualTo(loanRoot.get("startDate"), endDate),
                    cb.greaterThanOrEqualTo(loanRoot.get("endDate"), startDate)
            );

            // Condition pour lier les emprunts à l'élément
            loanSubquery.where(cb.and(
                    loanOverlap,
                    cb.equal(loanRoot.get("item"), root)
            ));

            // Ajouter la condition pour exclure les éléments ayant des emprunts qui se chevauchent
            predicates.add(cb.not(cb.exists(loanSubquery)));
        }

        if (rating != null) {
            // Sous-requête pour calculer la note moyenne des emprunts de chaque élément
            Subquery<Double> ratingSubquery = cr.subquery(Double.class);
            Root<Loan> ratingRoot = ratingSubquery.from(Loan.class);
            ratingSubquery.select(cb.avg(ratingRoot.get("rating")));

            // Condition pour lier les emprunts à l'élément
            ratingSubquery.where(cb.equal(ratingRoot.get("item"), root));

            // Ajouter la condition pour inclure uniquement les éléments ayant une note moyenne supérieure
            predicates.add(cb.greaterThanOrEqualTo(ratingSubquery, rating.doubleValue()));
        }

        if (type != null) {
            predicates.add(cb.equal(root.get("isService"), type.equals("Service")));
        }

        cr.where(cb.and(predicates.toArray(new Predicate[0])));
        Query query = em.createQuery(cr);
        return query.getResultList();
    }

    @Override
    public List<Element> search(Element criteria) {
        return null;
    }
}