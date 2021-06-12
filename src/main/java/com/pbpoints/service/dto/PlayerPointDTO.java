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

    private TournamentDTO tournament;

    private UserDTO user;

    private CategoryDTO category;

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

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
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
            getTournament() +
            "'" +
            ", user=" +
            getUser() +
            "'" +
            ", category=" +
            getCategory() +
            "'" +
            "}"
        );
    }
}
