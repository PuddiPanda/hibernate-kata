package org.squashtest.hibernatekata.jpa.exercises;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.dto.FilmDto;
import org.squashtest.hibernatekata.jpa.dto.FilmProjection;
import org.squashtest.hibernatekata.jpa.dto.FilmWithLanguageDto;
import org.squashtest.hibernatekata.jpa.entities.Address;
import org.squashtest.hibernatekata.jpa.entities.Film;
import org.squashtest.hibernatekata.jpa.repository.FilmRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ManyToOneTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManyToOneTest.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // Columns fetched ? Why ?
    // Change to Lazy... what's happened
    @Test
    public void loadFilmAfterCreatingManyToOneAssoc() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Film film = entityManager.find(Film.class, 1);
        new FilmWithLanguageDto(film.getId(), film.getTitle(), film.getLanguage().getName());
        LOGGER.info("---------- END TEST -----------");
    }

    // How many request ? Why ?
    // why we have already loose control on data fetch ?
    @Test
    public void onRequestLoadingWithoutFetch() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Film> films = entityManager.createQuery("select f from Film f", Film.class)
                .getResultList();
        List<FilmWithLanguageDto> dtos = films.stream()
                .map(film -> new FilmWithLanguageDto(film.getId(), film.getTitle(), film.getLanguage().getName()))
                .collect(Collectors.toList());
        assertEquals(1000, dtos.size());
        LOGGER.info("---------- END TEST -----------");
    }

    // how many request ? why ?
    @Test
    public void onRequestLoadingWithFetch() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Film> films = entityManager.createQuery("select f from Film f join fetch f.language", Film.class)
                .getResultList();
        List<FilmWithLanguageDto> dtos = films.stream()
                .map(film -> new FilmWithLanguageDto(film.getId(), film.getTitle(), film.getLanguage().getName()))
                .collect(Collectors.toList());
        assertEquals(1000, dtos.size());
        LOGGER.info("---------- END TEST -----------");
    }

    // how many request ? wtf ? what happened ?
    // why so many request compared to film requests ?
    // try with lazy... why it will probably not solve our problem every time ?
    @Test
    public void onLoadingAddressWithoutJoinFetch() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Address> address = entityManager.createQuery("select a from Address a", Address.class)
                .getResultList();
        LOGGER.info("---------- END TEST -----------");
    }

    // how many request ?
    @Test
    public void onLoadingAddressWithJoinFetch() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Address> address = entityManager.createQuery("select a from Address a join fetch a.city c join fetch c.country", Address.class)
                .getResultList();
        LOGGER.info("---------- END TEST -----------");
    }
}
