package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.TeamCategoryPoint} entity.
 */
public class TeamCategoryPointDTO implements Serializable {

    private Long id;

    private Float points;

    private Integer position;

    private TeamDetailPointDTO teamDetailPoint;

    private EventCategoryDTO eventCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public TeamDetailPointDTO getTeamDetailPoint() {
        return teamDetailPoint;
    }

    public void setTeamDetailPoint(TeamDetailPointDTO teamDetailPoint) {
        this.teamDetailPoint = teamDetailPoint;
    }

    public EventCategoryDTO getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategoryDTO eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamCategoryPointDTO)) {
            return false;
        }

        TeamCategoryPointDTO teamCategoryPointDTO = (TeamCategoryPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamCategoryPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamCategoryPointDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", position=" + getPosition() +
            ", teamDetailPoint=" + getTeamDetailPoint() +
            ", eventCategory=" + getEventCategory() +
            "}";
    }
}
