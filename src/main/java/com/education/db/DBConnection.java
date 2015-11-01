package com.education.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import javax.print.ServiceUIFactory;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 10/31/15.
 */
public class DBConnection {

    private static SessionFactory sessionFactory;

    private static ThreadLocal<Session> sessionThread = new ThreadLocal<>();

    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

    static {
        Configuration configuration = new Configuration().configure(getConfigFile());
        ServiceRegistryBuilder builder = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static Session getCurrentSession() {
        Session session = sessionThread.get();
        if (session == null) {
            session = sessionFactory.openSession();
            sessionThread.set(session);
        }
        return session;
    }

    public static void closeSession() {
        Session session = sessionThread.get();
        if (session != null) {
            session.close();
            sessionThread.remove();
        }
    }

    private static String getConfigFile() {
        String cfgFile = System.getProperty("HIBERNATE_CFG_FILE");
        if (cfgFile == null) {
            cfgFile = "hibernate.cfg.xml";
        }
        logger.info("get config file "+cfgFile);
        return cfgFile;
    }

}
