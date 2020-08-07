package org.squashtest.hibernatekata.jpa.entities;

import javax.persistence.*;

@Entity(name = "actor")
public class Actor {

    @Id
    @Column(name = "actor_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
