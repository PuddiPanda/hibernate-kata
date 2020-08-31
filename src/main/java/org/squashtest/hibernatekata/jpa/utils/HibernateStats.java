package org.squashtest.hibernatekata.jpa.utils;

import org.hibernate.Session;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class HibernateStats {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateStats.class);

    public static SessionStatistics getSessionStats(EntityManager entityManager) {
        Session session = entityManager.unwrap(Session.class);
        return session.getStatistics();
    }

    public static void logEntityCount(EntityManager entityManager) {
        SessionStatistics sessionStatistics = getSessionStats(entityManager);
        LOGGER.info("{} entities in first level cache.", sessionStatistics.getEntityCount());

    }

    public static Statistics getFactoryStats(EntityManager entityManager) {
        Session session = entityManager.unwrap(Session.class);
        return session.getSessionFactory().getStatistics();
    }
}
