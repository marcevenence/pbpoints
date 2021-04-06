package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.PlayerDetailPoint} entity.
 */
public class PlayerDetailPointDTO implements Serializable {

    private Long id;

    @NotNull
    private Float points;

    private Long eventId;

    private String eventName;

    private Long playerPointId;

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

    public Long getPlayerPointId() {
        return playerPointId;
    }

    public void setPlayerPointId(Long playerPointId) {
        this.playerPointId = playerPointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerDetailPointDTO playerDetailPointDTO = (PlayerDetailPointDTO) o;
        if (playerDetailPointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerDetailPointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "PlayerDetailPointDTO{" +
            "id=" +
            getId() +
            ", points=" +
            getPoints() +
            ", event=" +
            getEventId() +
            ", event='" +
            getEventName() +
            "'" +
            ", playerPoint=" +
            getPlayerPointId() +
            "}"
        );
    }
}
