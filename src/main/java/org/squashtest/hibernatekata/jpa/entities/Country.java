package org.squashtest.hibernatekata.jpa.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name = "country-with-cities" , attributeNodes = {
                @NamedAttributeNode("cities")
        }),
        @NamedEntityGraph(
                name = "country-with-cities-with-addresses" ,
                attributeNodes = {@NamedAttributeNode(value = "cities", subgraph = "cities-with-addresses")},
                subgraphs = {@NamedSubgraph(name = "cities-with-addresses", attributeNodes = {@NamedAttributeNode("addresses")})}
                )
})
public class Country {
    @Id
    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "country_country_id_seq")
    @SequenceGenerator(name = "country_country_id_seq", sequenceName = "country_country_id_seq", allocationSize = 1)
    private Integer id;

    private String country;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @OneToMany(mappedBy = "country")
    private Set<City> cities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}
