package org.squashtest.hibernatekata.jpa.exercises;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.dto.FilmWithLanguageDto;
import org.squashtest.hibernatekata.jpa.entities.Address;
import org.squashtest.hibernatekata.jpa.entities.Country;
import org.squashtest.hibernatekata.jpa.entities.Film;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OneToMany {

    private static final Logger LOGGER = LoggerFactory.getLogger(OneToMany.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // why two request only ?
    @Test
    public void loadAllAddressFromOneCountry() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Country country = entityManager.find(Country.class, 103);
        country.getCities().forEach(city -> LOGGER.info(city.getCity()));
        LOGGER.info("---------- END TEST -----------");
    }

    // Why more than two request ? what's happened ?
    @Test
    public void requestAllAddressFromOneCountry() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Country country = entityManager.find(Country.class, 103);
        country.getCities().forEach(city -> LOGGER.info(city.toString()));
        LOGGER.info("---------- END TEST -----------");
    }

    // How many request ? Why ?
    @Test
    public void requestAllCitiesForAllCountries() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Country> countries = entityManager.createQuery("select c from Country c", Country.class)
                .getResultList();
        countries.forEach(country -> country.getCities().forEach(city -> LOGGER.info(city.toString())));
        LOGGER.info("---------- END TEST -----------");
    }
}
