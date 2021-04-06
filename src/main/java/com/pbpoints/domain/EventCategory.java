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
 * A EventCategory.
 */
@Entity
@Table(name = "event_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "split_deck")
    private Boolean splitDeck;

    @OneToMany(mappedBy = "eventCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "teamA", "teamB", "eventCategory" }, allowSetters = true)
    private Set<Game> games = new HashSet<>();

    @OneToMany(mappedBy = "eventCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "players", "team", "eventCategory" }, allowSetters = true)
    private Set<Roster> rosters = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "city", "tournament" }, allowSetters = true)
    private Event event;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Category category;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Format format;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventCategory id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getSplitDeck() {
        return this.splitDeck;
    }

    public EventCategory splitDeck(Boolean splitDeck) {
        this.splitDeck = splitDeck;
        return this;
    }

    public void setSplitDeck(Boolean splitDeck) {
        this.splitDeck = splitDeck;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public EventCategory games(Set<Game> games) {
        this.setGames(games);
        return this;
    }

    public EventCategory addGame(Game game) {
        this.games.add(game);
        game.setEventCategory(this);
        return this;
    }

    public EventCategory removeGame(Game game) {
        this.games.remove(game);
        game.setEventCategory(null);
        return this;
    }

    public void setGames(Set<Game> games) {
        if (this.games != null) {
            this.games.forEach(i -> i.setEventCategory(null));
        }
        if (games != null) {
            games.forEach(i -> i.setEventCategory(this));
        }
        this.games = games;
    }

    public Set<Roster> getRosters() {
        return this.rosters;
    }

    public EventCategory rosters(Set<Roster> rosters) {
        this.setRosters(rosters);
        return this;
    }

    public EventCategory addRoster(Roster roster) {
        this.rosters.add(roster);
        roster.setEventCategory(this);
        return this;
    }

    public EventCategory removeRoster(Roster roster) {
        this.rosters.remove(roster);
        roster.setEventCategory(null);
        return this;
    }

    public void setRosters(Set<Roster> rosters) {
        if (this.rosters != null) {
            this.rosters.forEach(i -> i.setEventCategory(null));
        }
        if (rosters != null) {
            rosters.forEach(i -> i.setEventCategory(this));
        }
        this.rosters = rosters;
    }

    public Event getEvent() {
        return this.event;
    }

    public EventCategory event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Category getCategory() {
        return this.category;
    }

    public EventCategory category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Format getFormat() {
        return this.format;
    }

    public EventCategory format(Format format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCategory)) {
            return false;
        }
        return id != null && id.equals(((EventCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCategory{" +
            "id=" + getId() +
            ", splitDeck='" + getSplitDeck() + "'" +
            "}";
    }
}
