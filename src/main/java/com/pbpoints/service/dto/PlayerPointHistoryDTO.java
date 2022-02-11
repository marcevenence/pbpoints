package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.PlayerPointHistory} entity.
 */
public class PlayerPointHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Float points;

    private PlayerPointDTO playerPoint;

    private CategoryDTO category;

    private SeasonDTO season;

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

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SeasonDTO getSeason() {
        return season;
    }

    public void setSeason(SeasonDTO season) {
        this.season = season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerPointHistoryDTO)) {
            return false;
        }

        PlayerPointHistoryDTO playerPointHistoryDTO = (PlayerPointHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, playerPointHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerPointHistoryDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", playerPoint=" + getPlayerPoint() +
            ", category=" + getCategory() +
            ", season=" + getSeason() +
            "}";
    }
}
