package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlayerPointHistory.
 */
@Entity
@Table(name = "player_point_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayerPointHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tournament", "user", "category" }, allowSetters = true)
    private PlayerPoint playerPoint;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Season season;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerPointHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoints() {
        return this.points;
    }

    public PlayerPointHistory points(Float points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public PlayerPoint getPlayerPoint() {
        return this.playerPoint;
    }

    public void setPlayerPoint(PlayerPoint playerPoint) {
        this.playerPoint = playerPoint;
    }

    public PlayerPointHistory playerPoint(PlayerPoint playerPoint) {
        this.setPlayerPoint(playerPoint);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PlayerPointHistory category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public PlayerPointHistory season(Season season) {
        this.setSeason(season);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerPointHistory)) {
            return false;
        }
        return id != null && id.equals(((PlayerPointHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerPointHistory{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
