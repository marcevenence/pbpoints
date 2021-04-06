package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlayerPoint.
 */
@Entity
@Table(name = "player_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayerPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournament;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerPoint id(Long id) {
        this.id = id;
        return this;
    }

    public Float getPoints() {
        return this.points;
    }

    public PlayerPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public PlayerPoint tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public User getUser() {
        return this.user;
    }

    public PlayerPoint user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return this.category;
    }

    public PlayerPoint category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerPoint)) {
            return false;
        }
        return id != null && id.equals(((PlayerPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerPoint{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
