package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.TeamPoint} entity.
 */
public class TeamPointDTO implements Serializable {

    private Long id;

    @NotNull
    private Float points;

    private Long teamId;

    private String teamName;

    private Long tournamentId;

    private String tournamentName;

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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamPointDTO teamPointDTO = (TeamPointDTO) o;
        if (teamPointDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamPointDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "TeamPointDTO{" +
            "id=" +
            getId() +
            ", points=" +
            getPoints() +
            ", team=" +
            getTeamId() +
            ", team='" +
            getTeamName() +
            "'" +
            ", tournament=" +
            getTournamentId() +
            ", tournament='" +
            getTournamentName() +
            "'" +
            "}"
        );
    }
}
