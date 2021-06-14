package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.ProfileUser;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile")
    private ProfileUser profile;

    @ManyToOne
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "players", "team", "eventCategory" }, allowSetters = true)
    private Roster roster;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tournament" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player id(Long id) {
        this.id = id;
        return this;
    }

    public ProfileUser getProfile() {
        return this.profile;
    }

    public Player profile(ProfileUser profile) {
        this.profile = profile;
        return this;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public User getUser() {
        return this.user;
    }

    public Player user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Roster getRoster() {
        return this.roster;
    }

    public Player roster(Roster roster) {
        this.setRoster(roster);
        return this;
    }

    public void setRoster(Roster roster) {
        this.roster = roster;
    }

    public Category getCategory() {
        return this.category;
    }

    public Player category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", profile='" + getProfile() + "'" +
            "}";
    }
}
