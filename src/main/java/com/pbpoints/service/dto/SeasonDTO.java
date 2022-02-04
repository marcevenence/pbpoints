package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Season} entity.
 */
public class SeasonDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer anio;

    @NotNull
    private Status status;

    private TournamentDTO tournament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if (!(o instanceof SeasonDTO)) {
            return false;
        }

        SeasonDTO seasonDTO = (SeasonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, seasonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeasonDTO{" +
            "id=" + getId() +
            ", anio=" + getAnio() +
            ", status='" + getStatus() + "'" +
            ", tournament=" + getTournament() +
            "}";
    }
}
