package fr._14.pcd.codingweek15.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;


public abstract class DAO<T> {

    protected Class<T> modelClass;

    private final SessionFactory sf;

    public DAO(SessionFactory sf) {
        this.sf = sf;
    }

    protected final Session getSession() {
        Session session = null;
        try {
            session = this.sf.getCurrentSession();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (session == null)
            session = sf.openSession();

        return session;

    }

    protected final Transaction getTransaction(Session session) {
        Transaction tx = session.getTransaction();
        if (!TransactionStatus.ACTIVE.equals(tx.getStatus()))
            tx = session.beginTransaction();

        return tx;
    }

    public final Long create(T obj) {
        Session session = this.getSession();
        Transaction tx = this.getTransaction(session);
        Long id = (Long) session.save(obj);
        tx.commit();
        return id;
    }

    public final void delete(T obj) {
        Session session = this.getSession();
        Transaction tx = this.getTransaction(session);
        session.delete(obj);
        tx.commit();
    }

    public final void update(T obj) {
        Session session = this.getSession();
        Transaction tx = this.getTransaction(session);
        session.update(obj);
        tx.commit();
    }

    public T getById(Long id) {
        return getSession().get(modelClass, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getSession().createQuery("from " +
                modelClass.getName()).list();
    }

    public abstract List<T> search(T criteria);
}