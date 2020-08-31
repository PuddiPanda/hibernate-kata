package org.squashtest.hibernatekata.jpa.exercises;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.entities.Film;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class SimpleModificationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleModificationTest.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // What is the request order ? Why ?
    // Whats does it means for debugging our service code ?
    @Test
    public void modifyOneEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.find(Film.class, 7);
        // No automatic flush in test mode
        // However in real app, the transactional boundary flush the current session
        entityManager.flush();
        entityManager.clear();
        Film filmReloaded = entityManager.find(Film.class, 1);
        assertEquals("WIZARD ACADEMY", filmReloaded.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // Why the modification is not saved ?
    @Test
    public void clearBeforeFlushingEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.clear();
        entityManager.flush();
        Film filmReloaded = entityManager.find(Film.class, 1);
        assertNotEquals("WIZARD ACADEMY", filmReloaded.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // Why the modification is saved ?
    @Test
    public void selectBeforeCleaningEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.createQuery("select f from Film f where f.title like 'AIRPLANE SIERRA'").getResultList();
        entityManager.clear();
        Film filmReloaded = entityManager.find(Film.class, 1);
        assertEquals("WIZARD ACADEMY", filmReloaded.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // Why the modification is not saved ? Hibernate can be smarter than you think...
    @Test
    public void selectDifferentRequestBeforeCleaningEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.createQuery("select a from Actor a where a.firstName like 'PENELOPE'").getResultList();
        entityManager.clear();
        Film filmReloaded = entityManager.find(Film.class, 1);
        assertNotEquals("WIZARD ACADEMY", filmReloaded.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // Why the modification is not saved ?
    @Test
    public void loadAnotherBeforeCleaningEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.find(Film.class, 7);
        entityManager.clear();
        Film filmReloaded = entityManager.find(Film.class, 1);
        assertNotEquals("WIZARD ACADEMY", filmReloaded.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // just for exploring flush mode
    public void lookingFlushMode() {
        Session session = entityManager.unwrap(Session.class);
        session.getFlushMode();
    }

    // Simple fetch time
    @Test
    public void dirtyCheckingWithOneEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.flush();
        LOGGER.info("---------- END TEST -----------");
    }

    // more entity at fetch time
    @Test
    public void dirtyCheckingIsReallyExpensive() {
        LOGGER.info("---------- BEGIN TEST -----------");
        entityManager.createQuery("select a from Actor a").getResultList();
        entityManager.createQuery("select f from Film f").getResultList();
        Film film = entityManager.find(Film.class, 1);
        assertEquals("ACADEMY DINOSAUR", film.getTitle());
        film.setTitle("WIZARD ACADEMY");
        entityManager.flush();
        LOGGER.info("---------- END TEST -----------");
    }
}
