package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TeamCategoryPoint.
 */
@Entity
@Table(name = "team_category_point")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TeamCategoryPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points")
    private Float points;

    @Column(name = "position")
    private Integer position;

    @ManyToOne
    @JsonIgnoreProperties(value = { "teamPoint", "event" }, allowSetters = true)
    private TeamDetailPoint teamDetailPoint;

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

    public TeamCategoryPoint id(Long id) {
        this.id = id;
        return this;
    }

    public Float getPoints() {
        return this.points;
    }

    public TeamCategoryPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Integer getPosition() {
        return this.position;
    }

    public TeamCategoryPoint position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public TeamDetailPoint getTeamDetailPoint() {
        return this.teamDetailPoint;
    }

    public TeamCategoryPoint teamDetailPoint(TeamDetailPoint teamDetailPoint) {
        this.setTeamDetailPoint(teamDetailPoint);
        return this;
    }

    public void setTeamDetailPoint(TeamDetailPoint teamDetailPoint) {
        this.teamDetailPoint = teamDetailPoint;
    }

    public EventCategory getEventCategory() {
        return this.eventCategory;
    }

    public TeamCategoryPoint eventCategory(EventCategory eventCategory) {
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
        if (!(o instanceof TeamCategoryPoint)) {
            return false;
        }
        return id != null && id.equals(((TeamCategoryPoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamCategoryPoint{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", position=" + getPosition() +
            "}";
    }
}
