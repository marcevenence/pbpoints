package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Game} entity.
 */
public class GameDTO implements Serializable {

    private Long id;

    private Integer pointsA;

    private Integer pointsB;

    private Integer splitDeckNum;

    private Integer timeLeft;

    @NotNull
    private Status status;

    private Integer overtimeA;

    private Integer overtimeB;

    private Integer uvuA;

    private Integer uvuB;

    private Integer group;

    private String clasif;

    private TeamDTO teamA;

    private TeamDTO teamB;

    private EventCategoryDTO eventCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getOvertimeA() {
        return overtimeA;
    }

    public void setOvertimeA(Integer overtimeA) {
        this.overtimeA = overtimeA;
    }

    public Integer getOvertimeB() {
        return overtimeB;
    }

    public void setOvertimeB(Integer overtimeB) {
        this.overtimeB = overtimeB;
    }

    public Integer getUvuA() {
        return uvuA;
    }

    public void setUvuA(Integer uvuA) {
        this.uvuA = uvuA;
    }

    public Integer getUvuB() {
        return uvuB;
    }

    public void setUvuB(Integer uvuB) {
        this.uvuB = uvuB;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getClasif() {
        return clasif;
    }

    public void setClasif(String clasif) {
        this.clasif = clasif;
    }

    public TeamDTO getTeamA() {
        return teamA;
    }

    public void setTeamA(TeamDTO teamA) {
        this.teamA = teamA;
    }

    public TeamDTO getTeamB() {
        return teamB;
    }

    public void setTeamB(TeamDTO teamB) {
        this.teamB = teamB;
    }

    public EventCategoryDTO getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategoryDTO eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameDTO)) {
            return false;
        }

        GameDTO gameDTO = (GameDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gameDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDTO{" +
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
            ", teamA=" + getTeamA() +
            ", teamB=" + getTeamB() +
            ", eventCategory=" + getEventCategory() +
            "}";
    }
}
