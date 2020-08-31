package org.squashtest.hibernatekata.jpa.exercises;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.dto.FilmDto;
import org.squashtest.hibernatekata.jpa.dto.FilmProjection;
import org.squashtest.hibernatekata.jpa.entities.Film;
import org.squashtest.hibernatekata.jpa.repository.FilmRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class ProjectionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectionTest.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    FilmRepository filmRepository;

    // Witch columns are fetched ? Why ?
    @Test
    public void loadOneEntityAndExtract() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        FilmDto filmDto = new FilmDto(film.getId(), film.getTitle());
        assertEquals(1, filmDto.getId());
        assertEquals("ACADEMY DINOSAUR", filmDto.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }

    // Witch columns are fetched ? Why ?
    @Test
    public void projectOneEntity() {
        LOGGER.info("---------- BEGIN TEST -----------");
        FilmDto filmDto = entityManager
                .createQuery("select new org.squashtest.hibernatekata.jpa.dto.FilmDto(f.id, f.title) " +
                        "from Film f where f.id = 1", FilmDto.class)
                .getSingleResult();
        assertEquals(1, filmDto.getId());
        assertEquals("ACADEMY DINOSAUR", filmDto.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }


    // Witch columns are fetched ? Why ?
    @Test
    public void projectOneEntityWithSpringData() {
        LOGGER.info("---------- BEGIN TEST -----------");
        FilmProjection filmProjection = this.filmRepository.findFilmProjectionById(1).get();
        assertEquals(1, filmProjection.getId());
        assertEquals("ACADEMY DINOSAUR", filmProjection.getTitle());
        LOGGER.info("---------- END TEST -----------");
    }
}
