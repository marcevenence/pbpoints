package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Season.
 */
@Entity
@Table(name = "season")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Season implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "anio", nullable = false)
    private Integer anio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Season id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnio() {
        return this.anio;
    }

    public Season anio(Integer anio) {
        this.setAnio(anio);
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Status getStatus() {
        return this.status;
    }

    public Season status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Tournament getTournament() {
        return this.tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Season tournament(Tournament tournament) {
        this.setTournament(tournament);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Season)) {
            return false;
        }
        return id != null && id.equals(((Season) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Season{" +
            "id=" + getId() +
            ", anio=" + getAnio() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
