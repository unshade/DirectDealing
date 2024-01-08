package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.Element;
import fr._14.pcd.codingweek15.model.Loan;
import fr._14.pcd.codingweek15.model.User;
import fr._14.pcd.codingweek15.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public final class LoanDAO extends DAO<Loan> {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private static LoanDAO instance;

    private LoanDAO(SessionFactory sf) {
        super(sf);
        emf = Persistence.createEntityManagerFactory("loan");
        em = emf.createEntityManager();
        instance = this;
    }

    public static LoanDAO getInstance() {
        if (instance == null) {
            instance = new LoanDAO(HibernateUtil.getSessionFactory());
        }
        return instance;
    }

    public Loan createLoan(Date startDate, Date endDate, Element item, User borrower) {
        em.getTransaction().begin();
        Loan loan = new Loan(startDate, endDate, item, borrower);
        em.persist(loan);
        em.getTransaction().commit();
        return loan;
    }

    public List<Loan> getAllLoans() {
        return em.createQuery("SELECT u FROM Loan u", Loan.class).getResultList();
    }

    @Override
    public List<Loan> search(Loan criteria) {
        return null;
    }
}