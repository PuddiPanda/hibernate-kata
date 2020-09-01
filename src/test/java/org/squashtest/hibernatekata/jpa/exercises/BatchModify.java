package org.squashtest.hibernatekata.jpa.exercises;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.dto.FilmWithLanguageDto;
import org.squashtest.hibernatekata.jpa.entities.Address;
import org.squashtest.hibernatekata.jpa.entities.City;
import org.squashtest.hibernatekata.jpa.entities.Country;
import org.squashtest.hibernatekata.jpa.entities.Film;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class BatchModify {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchModify.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    // Basic fail...
    // What about flush time ?
    @Test
    public void modifyEntities() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Country> countries = entityManager.createQuery("select c from Country c", Country.class)
                .getResultList();
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            country.setCountry("COUNTRY_" + i);
        }
        entityManager.flush();
        LOGGER.info("---------- END TEST -----------");
    }

    // What about flush numbers and flush times ?
    @Test
    public void modifyEntitiesAndNestedOnes() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Country> countries = entityManager.createQuery("select c from Country c", Country.class)
                .getResultList();
        for (int i = 0; i < countries.size(); i++) {
            Country country = countries.get(i);
            country.setCountry("COUNTRY_" + i);
            List<City> cities = entityManager.createQuery("select c from City c where c.country.id = :id", City.class)
                    .setParameter("id", country.getId())
                    .getResultList();
            cities.forEach(city -> city.setLastUpdate(new Date()));
        }
        entityManager.flush();
        LOGGER.info("---------- END TEST -----------");
    }

    // Fixing at least for flushes
    // not bad but we could reduce again the flush time... how ?
    @Test
    public void batchModifyEntities() {
        LOGGER.info("---------- BEGIN TEST -----------");
        List<Integer> countryIds = entityManager.createQuery("select c.id from Country c", Integer.class)
                .getResultList();
        List<List<Integer>> partition = Lists.partition(countryIds, 10);
        partition.forEach(list -> {
            List<Country> countries = entityManager.createQuery("select c from Country c where c.id in :ids", Country.class)
                    .setParameter("ids", list)
                    .getResultList();
            for (int i = 0; i < countries.size(); i++) {
                Country country = countries.get(i);
                country.setCountry("COUNTRY_" + i);
                List<City> cities = entityManager.createQuery("select c from City c where c.country.id = :id", City.class)
                        .setParameter("id", country.getId())
                        .getResultList();
                cities.forEach(city -> city.setLastUpdate(new Date()));
            }
            entityManager.flush();
            entityManager.clear();
        });
        LOGGER.info("---------- END TEST -----------");
    }
}
