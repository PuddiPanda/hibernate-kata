package org.squashtest.hibernatekata.jpa.exercises;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.entities.City;
import org.squashtest.hibernatekata.jpa.entities.Country;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class BatchCreate {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchCreate.class);

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Test
    public void createEntities() {
        LOGGER.info("---------- BEGIN TEST -----------");
        IntStream stream = IntStream.range(0, 1000);
        stream.mapToObj(integer -> {
            Country country = new Country();
            country.setCountry("Country_" + integer);
            country.setLastUpdate(new Date());
            return country;
        }).forEach(country -> entityManager.persist(country));
        entityManager.flush();
        LOGGER.info("---------- END TEST -----------");
    }
}
