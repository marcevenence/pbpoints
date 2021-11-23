package com.pbpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points_a")
    private Integer pointsA;

    @Column(name = "points_b")
    private Integer pointsB;

    @Column(name = "split_deck_num")
    private Integer splitDeckNum;

    @Column(name = "time_left")
    private Integer timeLeft;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "overtime_a")
    private Integer overtimeA;

    @Column(name = "overtime_b")
    private Integer overtimeB;

    @Column(name = "uvu_a")
    private Integer uvuA;

    @Column(name = "uvu_b")
    private Integer uvuB;

    @Column(name = "jhi_group")
    private String group;

    @Column(name = "clasif")
    private String clasif;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Team teamA;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Team teamB;

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

    public Game id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPointsA() {
        return this.pointsA;
    }

    public Game pointsA(Integer pointsA) {
        this.pointsA = pointsA;
        return this;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return this.pointsB;
    }

    public Game pointsB(Integer pointsB) {
        this.pointsB = pointsB;
        return this;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getSplitDeckNum() {
        return this.splitDeckNum;
    }

    public Game splitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
        return this;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public Integer getTimeLeft() {
        return this.timeLeft;
    }

    public Game timeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
        return this;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return this.status;
    }

    public Game status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getOvertimeA() {
        return this.overtimeA;
    }

    public Game overtimeA(Integer overtimeA) {
        this.overtimeA = overtimeA;
        return this;
    }

    public void setOvertimeA(Integer overtimeA) {
        this.overtimeA = overtimeA;
    }

    public Integer getOvertimeB() {
        return this.overtimeB;
    }

    public Game overtimeB(Integer overtimeB) {
        this.overtimeB = overtimeB;
        return this;
    }

    public void setOvertimeB(Integer overtimeB) {
        this.overtimeB = overtimeB;
    }

    public Integer getUvuA() {
        return this.uvuA;
    }

    public Game uvuA(Integer uvuA) {
        this.uvuA = uvuA;
        return this;
    }

    public void setUvuA(Integer uvuA) {
        this.uvuA = uvuA;
    }

    public Integer getUvuB() {
        return this.uvuB;
    }

    public Game uvuB(Integer uvuB) {
        this.uvuB = uvuB;
        return this;
    }

    public void setUvuB(Integer uvuB) {
        this.uvuB = uvuB;
    }

    public String getGroup() {
        return this.group;
    }

    public Game group(String group) {
        this.group = group;
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getClasif() {
        return this.clasif;
    }

    public Game clasif(String clasif) {
        this.clasif = clasif;
        return this;
    }

    public void setClasif(String clasif) {
        this.clasif = clasif;
    }

    public Team getTeamA() {
        return this.teamA;
    }

    public Game teamA(Team team) {
        this.setTeamA(team);
        return this;
    }

    public void setTeamA(Team team) {
        this.teamA = team;
    }

    public Team getTeamB() {
        return this.teamB;
    }

    public Game teamB(Team team) {
        this.setTeamB(team);
        return this;
    }

    public void setTeamB(Team team) {
        this.teamB = team;
    }

    public EventCategory getEventCategory() {
        return this.eventCategory;
    }

    public Game eventCategory(EventCategory eventCategory) {
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
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", pointsA=" + getPointsA() +
            ", pointsB=" + getPointsB() +
            ", splitDeckNum=" + getSplitDeckNum() +
            ", timeLeft=" + getTimeLeft() +
            ", status='" + getStatus() + "'" +
            ", overtimeA=" + getOvertimeA() +
            ", overtimeB=" + getOvertimeB() +
            ", uvuA=" + getUvuA() +
            ", uvuB=" + getUvuB() +
            ", group=" + getGroup() +
            ", clasif='" + getClasif() + "'" +
            "}";
    }
}
