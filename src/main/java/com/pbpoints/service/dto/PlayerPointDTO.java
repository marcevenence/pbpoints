package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.PlayerPoint} entity.
 */
public class PlayerPointDTO implements Serializable {

    private Long id;

    @NotNull
    private Float points;

    private Long tournamentId;

    private String tournamentName;

    private Long userId;

    private String userLogin;

    private Long categoryId;

    private String categoryName;

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

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerPointDTO playerPointDTO = (PlayerPointDTO) o;
        if (playerPointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerPointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "PlayerPointDTO{" +
            "id=" +
            getId() +
            ", points=" +
            getPoints() +
            ", tournament=" +
            getTournamentId() +
            ", tournament='" +
            getTournamentName() +
            "'" +
            ", user=" +
            getUserId() +
            ", user='" +
            getUserLogin() +
            "'" +
            ", category=" +
            getCategoryId() +
            ", category='" +
            getCategoryName() +
            "'" +
            "}"
        );
    }
}
