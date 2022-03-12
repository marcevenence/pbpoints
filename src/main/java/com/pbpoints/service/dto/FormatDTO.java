package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Format} entity.
 */
public class FormatDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private Integer playersQty;

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

    public Integer getPlayersQty() {
        return playersQty;
    }

    public void setPlayersQty(Integer playersQty) {
        this.playersQty = playersQty;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormatDTO formatDTO = (FormatDTO) o;
        if (formatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "FormatDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", playersQty=" +
            getPlayersQty() +
            ", tournament=" +
            getTournament() +
            "'" +
            "}"
        );
    }
}
