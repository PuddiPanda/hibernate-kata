package org.squashtest.hibernatekata.jpa.entities;

import javax.persistence.*;

@Entity()
public class Actor {

    @Id
    @Column(name = "actor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
