package org.squashtest.hibernatekata.jpa.exercises;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.entities.Film;
import org.squashtest.hibernatekata.jpa.utils.HibernateStats;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class SimpleLoadsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLoadsTest.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // How many request fired ? Why ?
    @Test
    public void jpaFetchByEntityLoading() {
        Film film = entityManager.find(Film.class, 1);
        HibernateStats.logEntityCount(entityManager);
        Film filmBis = entityManager.find(Film.class, 1);
        HibernateStats.logEntityCount(entityManager);
    }

    // How many request fired ? Why ?
    @Test
    public void loadingAndClearingSession() {
        Film film = entityManager.find(Film.class, 1);
        HibernateStats.logEntityCount(entityManager);
        entityManager.clear();
        HibernateStats.logEntityCount(entityManager);
        Film filmBis = entityManager.find(Film.class, 1);
        HibernateStats.logEntityCount(entityManager);
    }

    // How many request fired ? Why ?
    @Test
    public void simpleGetReference() {
        Film filmRef = entityManager.getReference(Film.class, 1);
    }

    // How many request fired ? Why ?
    @Test
    public void simpleGetReferenceAndFetch() {
        Film filmRef = entityManager.getReference(Film.class, 1);
        filmRef.getDescription();
    }

    // just for explaining behavior
    @Test
    public void simpleFindNoExisting() {
        Film film = entityManager.find(Film.class, 0);
        assertNull(film);
    }

    // just for explaining behavior
    @Test
    public void simpleGetReferenceNoExisting() {
        assertThrows(EntityNotFoundException.class, () -> {
            Film film = entityManager.getReference(Film.class, 0);
            film.getDescription();
        });
    }

    // How many request fired ? Why ?
    @Test
    public void fetchWithQuery() {
        Film film = entityManager
                .createQuery("select film from Film film where film.id = :id", Film.class)
                .setParameter("id", 1)
                .getSingleResult();

        Film filmBis = entityManager
                .createQuery("select film from Film film where film.id = :id", Film.class)
                .setParameter("id", 1)
                .getSingleResult();
    }
}
