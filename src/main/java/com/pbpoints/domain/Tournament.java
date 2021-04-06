package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Tournament entity.\n@author Marcelo Miño
 */
@Entity
@Table(name = "tournament")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "close_inscr_days")
    private Integer closeInscrDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "categorize")
    private Boolean categorize;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "cant_players_next_category")
    private Integer cantPlayersNextCategory;

    @Column(name = "qty_team_groups")
    private Integer qtyTeamGroups;

    @OneToMany(mappedBy = "tournament")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "city", "tournament" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Tournament name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCloseInscrDays() {
        return this.closeInscrDays;
    }

    public Tournament closeInscrDays(Integer closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
        return this;
    }

    public void setCloseInscrDays(Integer closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
    }

    public Status getStatus() {
        return this.status;
    }

    public Tournament status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getCategorize() {
        return this.categorize;
    }

    public Tournament categorize(Boolean categorize) {
        this.categorize = categorize;
        return this;
    }

    public void setCategorize(Boolean categorize) {
        this.categorize = categorize;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Tournament logo(byte[] logo) {
        this.logo = logo;
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Tournament logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Integer getCantPlayersNextCategory() {
        return this.cantPlayersNextCategory;
    }

    public Tournament cantPlayersNextCategory(Integer cantPlayersNextCategory) {
        this.cantPlayersNextCategory = cantPlayersNextCategory;
        return this;
    }

    public void setCantPlayersNextCategory(Integer cantPlayersNextCategory) {
        this.cantPlayersNextCategory = cantPlayersNextCategory;
    }

    public Integer getQtyTeamGroups() {
        return this.qtyTeamGroups;
    }

    public Tournament qtyTeamGroups(Integer qtyTeamGroups) {
        this.qtyTeamGroups = qtyTeamGroups;
        return this;
    }

    public void setQtyTeamGroups(Integer qtyTeamGroups) {
        this.qtyTeamGroups = qtyTeamGroups;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public Tournament events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Tournament addEvent(Event event) {
        this.events.add(event);
        event.setTournament(this);
        return this;
    }

    public Tournament removeEvent(Event event) {
        this.events.remove(event);
        event.setTournament(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setTournament(null));
        }
        if (events != null) {
            events.forEach(i -> i.setTournament(this));
        }
        this.events = events;
    }

    public User getOwner() {
        return this.owner;
    }

    public Tournament owner(User user) {
        this.setOwner(user);
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tournament)) {
            return false;
        }
        return id != null && id.equals(((Tournament) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tournament{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", closeInscrDays=" + getCloseInscrDays() +
            ", status='" + getStatus() + "'" +
            ", categorize='" + getCategorize() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", cantPlayersNextCategory=" + getCantPlayersNextCategory() +
            ", qtyTeamGroups=" + getQtyTeamGroups() +
            "}";
    }
}
