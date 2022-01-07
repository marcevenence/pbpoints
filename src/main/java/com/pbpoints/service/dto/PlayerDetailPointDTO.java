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

    private PlayerPointDTO playerPoint;

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

    public PlayerPointDTO getPlayerPoint() {
        return playerPoint;
    }

    public void setPlayerPoint(PlayerPointDTO playerPoint) {
        this.playerPoint = playerPoint;
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
        if (!(o instanceof PlayerDetailPointDTO)) {
            return false;
        }

        PlayerDetailPointDTO playerDetailPointDTO = (PlayerDetailPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerDetailPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDetailPointDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", playerPoint=" + getPlayerPoint() +
            ", eventCategory=" + getEventCategory() +
            "}";
    }
}
