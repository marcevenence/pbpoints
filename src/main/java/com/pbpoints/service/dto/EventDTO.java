package com.pbpoints.service.dto;

import com.pbpoints.domain.Tournament;
import com.pbpoints.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.Event} entity.
 */
@ApiModel(description = "Event entity.\n@author Marcelo Miño")
public class EventDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate fromDate;

    private LocalDate endDate;

    private LocalDate endInscriptionDate;

    private Status status;

    private Instant createDate;

    private Instant updatedDate;

    private TournamentDTO tournament;

    private CityDTO city;

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

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (eventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "EventDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", fromDate='" +
            getFromDate() +
            "'" +
            ", endDate='" +
            getEndDate() +
            "'" +
            ", endInscriptionDate='" +
            getEndInscriptionDate() +
            "'" +
            ", status='" +
            getStatus() +
            "'" +
            ", createDate='" +
            getCreateDate() +
            "'" +
            ", updatedDate='" +
            getUpdatedDate() +
            "'" +
            ", tournament=" +
            getTournament() +
            ", city=" +
            getCity() +
            "}"
        );
    }
}
