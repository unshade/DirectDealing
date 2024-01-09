package fr._14.pcd.codingweek15.dao;

import fr._14.pcd.codingweek15.model.element.Element;
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
        super(Loan.class, sf);
        emf = Persistence.createEntityManagerFactory("michele");
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
        item.getLoans().add(loan);

        em.persist(loan);
        em.getTransaction().commit();
        return loan;
    }

    public List<Loan> getAllLoans() {
        return em.createQuery("SELECT u FROM Loan u", Loan.class).getResultList();
    }

    public List<Loan> getAllLoansByUser(User user) {
        return em.createQuery("SELECT u FROM Loan u WHERE u.borrower = :user", Loan.class).setParameter("user", user).getResultList();
    }

    public void dropTable() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Loan").executeUpdate();
        em.getTransaction().commit();
    }

    public void addRating(Loan loan, int rating) {
        em.getTransaction().begin();
        loan.setRating(rating);
        em.getTransaction().commit();
    }

    public List<Loan> getLoansByUserAndBorrowing(User owner) {
        return em.createQuery("SELECT u FROM Loan u WHERE u.item.owner = :user", Loan.class)
                .setParameter("user", owner)
                .getResultList();
    }

    @Override
    public List<Loan> search(Loan criteria) {
        return null;
    }
}