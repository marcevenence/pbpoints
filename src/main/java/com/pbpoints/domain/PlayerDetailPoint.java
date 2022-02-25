package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlayerDetailPoint.
 */
@Entity
@Table(name = "player_detail_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayerDetailPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament", "user", "category" }, allowSetters = true)
    private PlayerPoint playerPoint;

    @ManyToOne
    @JsonIgnoreProperties(value = { "games", "rosters", "event", "category", "format" }, allowSetters = true)
    private EventCategory eventCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerDetailPoint id(Long id) {
        this.id = id;
        return this;
    }

    public Float getPoints() {
        return this.points;
    }

    public PlayerDetailPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public PlayerPoint getPlayerPoint() {
        return this.playerPoint;
    }

    public PlayerDetailPoint playerPoint(PlayerPoint playerPoint) {
        this.setPlayerPoint(playerPoint);
        return this;
    }

    public void setPlayerPoint(PlayerPoint playerPoint) {
        this.playerPoint = playerPoint;
    }

    public EventCategory getEventCategory() {
        return this.eventCategory;
    }

    public PlayerDetailPoint eventCategory(EventCategory eventCategory) {
        this.setEventCategory(eventCategory);
        return this;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDetailPoint)) {
            return false;
        }
        return id != null && id.equals(((PlayerDetailPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDetailPoint{" +
            "id=" + getId() +
            ", eventCategory=" + getEventCategory() +
            ", points=" + getPoints() +
            "}";
    }
}
