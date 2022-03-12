package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Event entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "start_inscription_date")
    private LocalDate startInscriptionDate;

    @Column(name = "end_inscription_date")
    private LocalDate endInscriptionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @NotNull
    @Column(name = "end_inscription_players_date", nullable = false)
    private LocalDate endInscriptionPlayersDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournament;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "city" }, allowSetters = true)
    private Field field;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Season season;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFromDate() {
        return this.fromDate;
    }

    public Event fromDate(LocalDate fromDate) {
        this.setFromDate(fromDate);
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Event endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public LocalDate getStartInscriptionDate() {
        return startInscriptionDate;
    }

    public void setStartInscriptionDate(LocalDate startInscriptionDate) {
        this.startInscriptionDate = startInscriptionDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndInscriptionDate() {
        return this.endInscriptionDate;
    }

    public Event endInscriptionDate(LocalDate endInscriptionDate) {
        this.setEndInscriptionDate(endInscriptionDate);
        return this;
    }

    public void setEndInscriptionDate(LocalDate endInscriptionDate) {
        this.endInscriptionDate = endInscriptionDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public Event status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Event createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public Event updatedDate(Instant updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LocalDate getEndInscriptionPlayersDate() {
        return this.endInscriptionPlayersDate;
    }

    public Event endInscriptionPlayersDate(LocalDate endInscriptionPlayersDate) {
        this.setEndInscriptionPlayersDate(endInscriptionPlayersDate);
        return this;
    }

    public void setEndInscriptionPlayersDate(LocalDate endInscriptionPlayersDate) {
        this.endInscriptionPlayersDate = endInscriptionPlayersDate;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public Event tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Event field(Field field) {
        this.setField(field);
        return this;
    }

    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Event season(Season season) {
        this.setSeason(season);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
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
            "}";
    }
}
