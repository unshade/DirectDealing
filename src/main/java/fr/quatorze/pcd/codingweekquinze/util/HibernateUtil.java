package fr.quatorze.pcd.codingweekquinze.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    @Getter
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = buildSessionFactory();
    }

    public static void recreateSessionFactory(Configuration configuration) {
        sessionFactory.close();
        sessionFactory = configuration.buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
}
