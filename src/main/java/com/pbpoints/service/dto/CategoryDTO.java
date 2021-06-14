package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.TimeType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Category} entity.
 */
public class CategoryDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    @NotNull
    private TimeType gameTimeType;

    @NotNull
    private Integer gameTime;

    @NotNull
    private TimeType stopTimeType;

    @NotNull
    private Integer stopTime;

    @NotNull
    private Integer totalPoints;

    @NotNull
    private Integer difPoints;

    @NotNull
    private Integer order;

    private TournamentDTO tournament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TimeType getGameTimeType() {
        return gameTimeType;
    }

    public void setGameTimeType(TimeType gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public Integer getGameTime() {
        return gameTime;
    }

    public void setGameTime(Integer gameTime) {
        this.gameTime = gameTime;
    }

    public TimeType getStopTimeType() {
        return stopTimeType;
    }

    public void setStopTimeType(TimeType stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getDifPoints() {
        return difPoints;
    }

    public void setDifPoints(Integer difPoints) {
        this.difPoints = difPoints;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryDTO)) {
            return false;
        }

        CategoryDTO categoryDTO = (CategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryDTO{" +
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
            ", tournament=" + getTournament() +
            "}";
    }
}
