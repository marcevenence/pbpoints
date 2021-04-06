package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.TimeType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "game_time_type", nullable = false)
    private TimeType gameTimeType;

    @NotNull
    @Column(name = "game_time", nullable = false)
    private Integer gameTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stop_time_type", nullable = false)
    private TimeType stopTimeType;

    @NotNull
    @Column(name = "stop_time", nullable = false)
    private Integer stopTime;

    @NotNull
    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @NotNull
    @Column(name = "dif_points", nullable = false)
    private Integer difPoints;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Category description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeType getGameTimeType() {
        return this.gameTimeType;
    }

    public Category gameTimeType(TimeType gameTimeType) {
        this.gameTimeType = gameTimeType;
        return this;
    }

    public void setGameTimeType(TimeType gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public Integer getGameTime() {
        return this.gameTime;
    }

    public Category gameTime(Integer gameTime) {
        this.gameTime = gameTime;
        return this;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public TimeType getStopTimeType() {
        return this.stopTimeType;
    }

    public Category stopTimeType(TimeType stopTimeType) {
        this.stopTimeType = stopTimeType;
        return this;
    }

    public void setStopTimeType(TimeType stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public Integer getStopTime() {
        return this.stopTime;
    }

    public Category stopTime(Integer stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getTotalPoints() {
        return this.totalPoints;
    }

    public Category totalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getDifPoints() {
        return this.difPoints;
    }

    public Category difPoints(Integer difPoints) {
        this.difPoints = difPoints;
        return this;
    }

    public void setDifPoints(Integer difPoints) {
        this.difPoints = difPoints;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Category order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public Category tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", gameTimeType='" + getGameTimeType() + "'" +
            ", gameTime=" + getGameTime() +
            ", stopTimeType='" + getStopTimeType() + "'" +
            ", stopTime=" + getStopTime() +
            ", totalPoints=" + getTotalPoints() +
            ", difPoints=" + getDifPoints() +
            ", order=" + getOrder() +
            "}";
    }
}
