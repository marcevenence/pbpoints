package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pbpoints.domain.Game} entity. This class is used
 * in {@link com.pbpoints.web.rest.GameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /games?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GameCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter pointsA;

    private IntegerFilter pointsB;

    private IntegerFilter splitDeckNum;

    private IntegerFilter timeLeft;

    private StatusFilter status;

    private IntegerFilter overtimeA;

    private IntegerFilter overtimeB;

    private IntegerFilter uvuA;

    private IntegerFilter uvuB;

    private IntegerFilter group;

    private StringFilter clasif;

    private LongFilter teamAId;

    private LongFilter teamBId;

    private LongFilter eventCategoryId;

    public GameCriteria() {}

    public GameCriteria(GameCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.pointsA = other.pointsA == null ? null : other.pointsA.copy();
        this.pointsB = other.pointsB == null ? null : other.pointsB.copy();
        this.splitDeckNum = other.splitDeckNum == null ? null : other.splitDeckNum.copy();
        this.timeLeft = other.timeLeft == null ? null : other.timeLeft.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.overtimeA = other.overtimeA == null ? null : other.overtimeA.copy();
        this.overtimeB = other.overtimeB == null ? null : other.overtimeB.copy();
        this.uvuA = other.uvuA == null ? null : other.uvuA.copy();
        this.uvuB = other.uvuB == null ? null : other.uvuB.copy();
        this.group = other.group == null ? null : other.group.copy();
        this.clasif = other.clasif == null ? null : other.clasif.copy();
        this.teamAId = other.teamAId == null ? null : other.teamAId.copy();
        this.teamBId = other.teamBId == null ? null : other.teamBId.copy();
        this.eventCategoryId = other.eventCategoryId == null ? null : other.eventCategoryId.copy();
    }

    @Override
    public GameCriteria copy() {
        return new GameCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPointsA() {
        return pointsA;
    }

    public void setPointsA(IntegerFilter pointsA) {
        this.pointsA = pointsA;
    }

    public IntegerFilter getPointsB() {
        return pointsB;
    }

    public void setPointsB(IntegerFilter pointsB) {
        this.pointsB = pointsB;
    }

    public IntegerFilter getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(IntegerFilter splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public IntegerFilter getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(IntegerFilter timeLeft) {
        this.timeLeft = timeLeft;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getOvertimeA() {
        return overtimeA;
    }

    public void setOvertimeA(IntegerFilter overtimeA) {
        this.overtimeA = overtimeA;
    }

    public IntegerFilter getOvertimeB() {
        return overtimeB;
    }

    public void setOvertimeB(IntegerFilter overtimeB) {
        this.overtimeB = overtimeB;
    }

    public IntegerFilter getUvuA() {
        return uvuA;
    }

    public void setUvuA(IntegerFilter uvuA) {
        this.uvuA = uvuA;
    }

    public IntegerFilter getUvuB() {
        return uvuB;
    }

    public void setUvuB(IntegerFilter uvuB) {
        this.uvuB = uvuB;
    }

    public IntegerFilter getGroup() {
        return group;
    }

    public void setGroup(IntegerFilter group) {
        this.group = group;
    }

    public StringFilter getClasif() {
        return clasif;
    }

    public void setClasif(StringFilter clasif) {
        this.clasif = clasif;
    }

    public LongFilter getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(LongFilter teamAId) {
        this.teamAId = teamAId;
    }

    public LongFilter getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(LongFilter teamBId) {
        this.teamBId = teamBId;
    }

    public LongFilter getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(LongFilter eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameCriteria that = (GameCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pointsA, that.pointsA) &&
            Objects.equals(pointsB, that.pointsB) &&
            Objects.equals(splitDeckNum, that.splitDeckNum) &&
            Objects.equals(timeLeft, that.timeLeft) &&
            Objects.equals(status, that.status) &&
            Objects.equals(overtimeA, that.overtimeA) &&
            Objects.equals(overtimeB, that.overtimeB) &&
            Objects.equals(uvuA, that.uvuA) &&
            Objects.equals(uvuB, that.uvuB) &&
            Objects.equals(group, that.group) &&
            Objects.equals(clasif, that.clasif) &&
            Objects.equals(teamAId, that.teamAId) &&
            Objects.equals(teamBId, that.teamBId) &&
            Objects.equals(eventCategoryId, that.eventCategoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pointsA,
            pointsB,
            splitDeckNum,
            timeLeft,
            status,
            overtimeA,
            overtimeB,
            uvuA,
            uvuB,
            group,
            clasif,
            teamAId,
            teamBId,
            eventCategoryId
        );
    }

    @Override
    public String toString() {
        return (
            "GameCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (pointsA != null ? "pointsA=" + pointsA + ", " : "") +
            (pointsB != null ? "pointsB=" + pointsB + ", " : "") +
            (splitDeckNum != null ? "splitDeckNum=" + splitDeckNum + ", " : "") +
            (timeLeft != null ? "timeLeft=" + timeLeft + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (overtimeA != null ? "overtimeA=" + overtimeA + ", " : "") +
            (overtimeB != null ? "overtimeB=" + overtimeB + ", " : "") +
            (uvuA != null ? "uvuA=" + uvuA + ", " : "") +
            (uvuB != null ? "uvuB=" + uvuB + ", " : "") +
            (group != null ? "group=" + group + ", " : "") +
            (clasif != null ? "clasif=" + clasif + ", " : "") +
            (teamAId != null ? "teamAId=" + teamAId + ", " : "") +
            (teamBId != null ? "teamBId=" + teamBId + ", " : "") +
            (eventCategoryId != null ? "eventCategoryId=" + eventCategoryId + ", " : "") +
            "}"
        );
    }
}
