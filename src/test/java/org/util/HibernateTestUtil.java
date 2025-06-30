package org.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testcontainers.containers.PostgreSQLContainer;

public class HibernateTestUtil {
    private static SessionFactory sessionFactory;

    public static void init(PostgreSQLContainer<?> postgres) {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public static void clearSession() {
        try (Session session = sessionFactory.openSession()) {
            session.clear();
        }
    }
}