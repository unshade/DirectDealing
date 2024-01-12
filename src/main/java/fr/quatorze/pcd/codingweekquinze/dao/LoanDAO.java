package fr.quatorze.pcd.codingweekquinze.dao;

import fr.quatorze.pcd.codingweekquinze.enums.LoanStatus;
import fr.quatorze.pcd.codingweekquinze.model.Element;
import fr.quatorze.pcd.codingweekquinze.model.Loan;
import fr.quatorze.pcd.codingweekquinze.model.User;
import fr.quatorze.pcd.codingweekquinze.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void endLoan(Loan loan) {
        em.getTransaction().begin();
        loan.setStatus(LoanStatus.ENDED.ordinal());
        em.merge(loan);
        em.getTransaction().commit();
    }

    public void accept(Loan loan) {
        em.getTransaction().begin();
        loan.setStatus(LoanStatus.ACCEPTED.ordinal());
        em.merge(loan);
        em.getTransaction().commit();
    }

    public void cancel(Loan loan) {
        em.getTransaction().begin();
        loan.setStatus(LoanStatus.CANCELED.ordinal());
        em.merge(loan);
        em.getTransaction().commit();
    }

    public Loan createLoan(LocalDateTime startDate, LocalDateTime endDate, Element item, User borrower) {
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

    public List<Loan> getAllLoansByUserNoOutdatedLoans(User user) {
        //LoanStatus is not CANCELED or ENDED
        int ended = LoanStatus.ENDED.ordinal();
        int canceled = LoanStatus.CANCELED.ordinal();
        //return em.createQuery("SELECT u FROM Loan u WHERE u.borrower = :user", Loan.class).setParameter("user", user).getResultList();
        return em.createQuery("SELECT u FROM Loan u WHERE u.borrower = :user AND u.status != :ended AND u.status != :canceled", Loan.class)
                .setParameter("user", user)
                .setParameter("ended", ended)
                .setParameter("canceled", canceled)
                .getResultList();
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

    public List<Loan> getLoansByUserAndBorrowingNoOutdated(User owner) {
        int ended = LoanStatus.ENDED.ordinal();
        int canceled = LoanStatus.CANCELED.ordinal();
        return em.createQuery("SELECT u FROM Loan u WHERE u.item.owner = :user AND u.status != :ended AND u.status != :canceled", Loan.class)
                .setParameter("user", owner)
                .setParameter("ended", ended)
                .setParameter("canceled", canceled)
                .getResultList();
    }

    @Override
    public List<Loan> search(Loan criteria) {
        return null;
    }
}