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

    private Long teamPointId;

    private Long eventId;

    private String eventName;

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

    public Long getTeamPointId() {
        return teamPointId;
    }

    public void setTeamPointId(Long teamPointId) {
        this.teamPointId = teamPointId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDetailPointDTO teamDetailPointDTO = (TeamDetailPointDTO) o;
        if (teamDetailPointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDetailPointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "TeamDetailPointDTO{" +
            "id=" +
            getId() +
            ", points=" +
            getPoints() +
            ", teamPoint=" +
            getTeamPointId() +
            ", event=" +
            getEventId() +
            ", event='" +
            getEventName() +
            "'" +
            "}"
        );
    }
}
