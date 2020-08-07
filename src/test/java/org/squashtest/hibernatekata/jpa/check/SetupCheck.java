package org.squashtest.hibernatekata.jpa.check;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.squashtest.hibernatekata.jpa.entities.Actor;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SetupCheck {

    @Autowired
    EntityManager entityManager;

    @Test
    public void checkDbAccess(){
        Actor actor = this.entityManager.find(Actor.class, 1);
        assertNotNull(actor);
    }
}
