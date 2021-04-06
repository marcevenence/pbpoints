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

    private TeamDTO team;

    private TournamentDTO tournament;

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

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
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
        if (!(o instanceof TeamPointDTO)) {
            return false;
        }

        TeamPointDTO teamPointDTO = (TeamPointDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, teamPointDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamPointDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", team=" + getTeam() +
            ", tournament=" + getTournament() +
            "}";
    }
}
