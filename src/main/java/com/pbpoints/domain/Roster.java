package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Roster entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "roster")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Roster implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "roster")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "roster" }, allowSetters = true)
    private Set<Player> players = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Team team;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "games", "rosters", "event", "category", "format" }, allowSetters = true)
    private EventCategory eventCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roster id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Roster active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }

    public Roster players(Set<Player> players) {
        this.setPlayers(players);
        return this;
    }

    public Roster addPlayer(Player player) {
        this.players.add(player);
        player.setRoster(this);
        return this;
    }

    public Roster removePlayer(Player player) {
        this.players.remove(player);
        player.setRoster(null);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        if (this.players != null) {
            this.players.forEach(i -> i.setRoster(null));
        }
        if (players != null) {
            players.forEach(i -> i.setRoster(this));
        }
        this.players = players;
    }

    public Team getTeam() {
        return this.team;
    }

    public Roster team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public EventCategory getEventCategory() {
        return this.eventCategory;
    }

    public Roster eventCategory(EventCategory eventCategory) {
        this.setEventCategory(eventCategory);
        return this;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roster)) {
            return false;
        }
        return id != null && id.equals(((Roster) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roster{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
