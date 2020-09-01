package org.squashtest.hibernatekata.jpa.entities;

import javax.persistence.*;
import java.util.Date;

@Entity()
public class Language {

    @Id
    @Column(name = "language_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "language_language_id_seq")
    @SequenceGenerator(name = "language_language_id_seq", sequenceName = "language_language_id_seq", allocationSize = 1)
    private Integer id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
