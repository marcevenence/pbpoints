package com.pbpoints.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bracket.
 */
@Entity
@Table(name = "bracket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bracket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "teams", nullable = false, unique = true)
    private Integer teams;

    @NotNull
    @Column(name = "teams_5_a", nullable = false)
    private Integer teams5A;

    @NotNull
    @Column(name = "teams_5_b", nullable = false)
    private Integer teams5B;

    @NotNull
    @Column(name = "teams_6_a", nullable = false)
    private Integer teams6A;

    @NotNull
    @Column(name = "teams_6_b", nullable = false)
    private Integer teams6B;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bracket id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getTeams() {
        return this.teams;
    }

    public Bracket teams(Integer teams) {
        this.teams = teams;
        return this;
    }

    public void setTeams(Integer teams) {
        this.teams = teams;
    }

    public Integer getTeams5A() {
        return this.teams5A;
    }

    public Bracket teams5A(Integer teams5A) {
        this.teams5A = teams5A;
        return this;
    }

    public void setTeams5A(Integer teams5A) {
        this.teams5A = teams5A;
    }

    public Integer getTeams5B() {
        return this.teams5B;
    }

    public Bracket teams5B(Integer teams5B) {
        this.teams5B = teams5B;
        return this;
    }

    public void setTeams5B(Integer teams5B) {
        this.teams5B = teams5B;
    }

    public Integer getTeams6A() {
        return this.teams6A;
    }

    public Bracket teams6A(Integer teams6A) {
        this.teams6A = teams6A;
        return this;
    }

    public void setTeams6A(Integer teams6A) {
        this.teams6A = teams6A;
    }

    public Integer getTeams6B() {
        return this.teams6B;
    }

    public Bracket teams6B(Integer teams6B) {
        this.teams6B = teams6B;
        return this;
    }

    public void setTeams6B(Integer teams6B) {
        this.teams6B = teams6B;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bracket)) {
            return false;
        }
        return id != null && id.equals(((Bracket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bracket{" +
            "id=" + getId() +
            ", teams=" + getTeams() +
            ", teams5A=" + getTeams5A() +
            ", teams5B=" + getTeams5B() +
            ", teams6A=" + getTeams6A() +
            ", teams6B=" + getTeams6B() +
            "}";
    }
}
