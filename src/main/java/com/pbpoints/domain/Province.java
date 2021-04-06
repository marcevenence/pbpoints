package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Province entity.\n@author Marcelo Miño\nDatos traidos desde https:
 */
@Entity
@Table(name = "province")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "province")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "province" }, allowSetters = true)
    private Set<City> cities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "provinces" }, allowSetters = true)
    private Country country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Province id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Province name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<City> getCities() {
        return this.cities;
    }

    public Province cities(Set<City> cities) {
        this.setCities(cities);
        return this;
    }

    public Province addCity(City city) {
        this.cities.add(city);
        city.setProvince(this);
        return this;
    }

    public Province removeCity(City city) {
        this.cities.remove(city);
        city.setProvince(null);
        return this;
    }

    public void setCities(Set<City> cities) {
        if (this.cities != null) {
            this.cities.forEach(i -> i.setProvince(null));
        }
        if (cities != null) {
            cities.forEach(i -> i.setProvince(this));
        }
        this.cities = cities;
    }

    public Country getCountry() {
        return this.country;
    }

    public Province country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return id != null && id.equals(((Province) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
