package me.dinuka.tmo.util;

import org.hibernate.SessionFactory;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        try {
            sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            throw new ExceptionInInitializerError("SessionFactory creation failed"+e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void shutdown(){
        sessionFactory.close();
    }
}
