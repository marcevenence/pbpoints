package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.TournamentGroup} entity.
 */
public class TournamentGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private TournamentDTO tournamentA;

    private TournamentDTO tournamentB;

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

    public TournamentDTO getTournamentA() {
        return tournamentA;
    }

    public void setTournamentA(TournamentDTO tournamentA) {
        this.tournamentA = tournamentA;
    }

    public TournamentDTO getTournamentB() {
        return tournamentB;
    }

    public void setTournamentB(TournamentDTO tournamentB) {
        this.tournamentB = tournamentB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TournamentGroupDTO)) {
            return false;
        }

        TournamentGroupDTO tournamentGroupDTO = (TournamentGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tournamentGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tournamentA=" + getTournamentA() +
            ", tournamentB=" + getTournamentB() +
            "}";
    }
}
