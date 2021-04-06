package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.TeamDetailPoint} entity.
 */
public class TeamDetailPointDTO implements Serializable {

    private Long id;

    @NotNull
    private Float points;

    private TeamPointDTO teamPoint;

    private EventDTO event;

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

    public TeamPointDTO getTeamPoint() {
        return teamPoint;
    }

    public void setTeamPoint(TeamPointDTO teamPoint) {
        this.teamPoint = teamPoint;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamDetailPointDTO)) {
            return false;
        }

        TeamDetailPointDTO teamDetailPointDTO = (TeamDetailPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamDetailPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamDetailPointDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", teamPoint=" + getTeamPoint() +
            ", event=" + getEvent() +
            "}";
    }
}
