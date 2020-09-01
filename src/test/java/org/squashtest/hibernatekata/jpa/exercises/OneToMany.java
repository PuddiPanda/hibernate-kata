package org.squashtest.hibernatekata.jpa.exercises;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.entities.Country;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Fixing method 1 with request-join
    @Test
    public void findAllCitiesFromOneCountryWithJoinFetch() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Map<String, Object> properties = getFetchProps("country-with-cities");
        Country country = entityManager.find(Country.class, 103, properties);
        country.getCities().forEach(city -> LOGGER.info(city.getCity()));
        LOGGER.info("---------- END TEST -----------");
    }

    // Fixing method 2 with load
    @Test
    public void findAllCitiesFromOneCountryWithEntityGraph() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Map<String, Object> properties = getFetchProps("country-with-cities");
        Country country = entityManager.find(Country.class, 103, properties);
        country.getCities().forEach(city -> LOGGER.info(city.getCity()));
        LOGGER.info("---------- END TEST -----------");
    }

    // Fixing method 3 with request
    // You can also do it with Spring Data or Criteria
    @Test
    public void requestAllCitiesFromOneCountryWithEntityGraph() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Map<String, Object> properties = getFetchProps("country-with-cities");
        Country country = entityManager.createQuery("select c from Country c where c.id = :id", Country.class)
                .setParameter("id", 103)
                .setHint("javax.persistence.loadgraph", entityManager.getEntityGraph("country-with-cities"))
                .getSingleResult();
        country.getCities().forEach(city -> LOGGER.info(city.getCity()));
        LOGGER.info("---------- END TEST -----------");
    }

    // Fixing when fetching all countries
    @Test
    public void requestAllCitiesFromAllCountriesWithEntityGraph() {
        LOGGER.info("---------- BEGIN TEST -----------");
        Map<String, Object> properties = getFetchProps("country-with-cities");
        List<Country> countries = entityManager.createQuery("select c from Country c", Country.class)
                .setHint("javax.persistence.loadgraph", entityManager.getEntityGraph("country-with-cities"))
                .getResultList();
        countries.forEach(country -> country.getCities().forEach(city -> LOGGER.info(city.getCity())));
        LOGGER.info("---------- END TEST -----------");
    }

    private Map<String, Object> getFetchProps(String graphName) {
        EntityGraph entityGraph = entityManager.getEntityGraph(graphName);
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);
        return properties;
    }
}
