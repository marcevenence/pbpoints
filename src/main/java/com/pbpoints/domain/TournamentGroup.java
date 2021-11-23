package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TournamentGroup.
 */
@Entity
@Table(name = "tournament_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TournamentGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournamentA;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "events", "owner" }, allowSetters = true)
    private Tournament tournamentB;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentGroup id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TournamentGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tournament getTournamentA() {
        return this.tournamentA;
    }

    public TournamentGroup tournamentA(Tournament tournament) {
        this.setTournamentA(tournament);
        return this;
    }

    public void setTournamentA(Tournament tournament) {
        this.tournamentA = tournament;
    }

    public Tournament getTournamentB() {
        return this.tournamentB;
    }

    public TournamentGroup tournamentB(Tournament tournament) {
        this.setTournamentB(tournament);
        return this;
    }

    public void setTournamentB(Tournament tournament) {
        this.tournamentB = tournament;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TournamentGroup)) {
            return false;
        }
        return id != null && id.equals(((TournamentGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
