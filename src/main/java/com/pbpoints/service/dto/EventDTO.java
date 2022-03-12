package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Event} entity.
 */
@Schema(description = "Event entity.\n@author Marcelo Miño")
public class EventDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate fromDate;

    private LocalDate endDate;

    private LocalDate startInscriptionDate;

    private LocalDate endInscriptionDate;

    private Status status;

    private Instant createDate;

    private Instant updatedDate;

    @NotNull
    private LocalDate endInscriptionPlayersDate;

    private TournamentDTO tournament;

    private FieldDTO field;

    private SeasonDTO season;

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

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartInscriptionDate() {
        return startInscriptionDate;
    }

    public void setStartInscriptionDate(LocalDate startInscriptionDate) {
        this.startInscriptionDate = startInscriptionDate;
    }

    public LocalDate getEndInscriptionDate() {
        return endInscriptionDate;
    }

    public void setEndInscriptionDate(LocalDate endInscriptionDate) {
        this.endInscriptionDate = endInscriptionDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDate getEndInscriptionPlayersDate() {
        return endInscriptionPlayersDate;
    }

    public void setEndInscriptionPlayersDate(LocalDate endInscriptionPlayersDate) {
        this.endInscriptionPlayersDate = endInscriptionPlayersDate;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public FieldDTO getField() {
        return field;
    }

    public void setField(FieldDTO field) {
        this.field = field;
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
        if (!(o instanceof EventDTO)) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", startInscriptionDate='" + getStartInscriptionDate() + "'" +
            ", endInscriptionDate='" + getEndInscriptionDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", endInscriptionPlayersDate='" + getEndInscriptionPlayersDate() + "'" +
            ", tournament=" + getTournament() +
            ", field=" + getField() +
            ", season=" + getSeason() +
            "}";
    }
}
