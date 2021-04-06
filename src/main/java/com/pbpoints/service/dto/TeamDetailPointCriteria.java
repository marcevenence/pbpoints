package com.pbpoints.service.dto;

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
 * Criteria class for the {@link com.pbpoints.domain.TeamDetailPoint} entity. This class is used
 * in {@link com.pbpoints.web.rest.TeamDetailPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /team-detail-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamDetailPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private LongFilter teamPointId;

    private LongFilter eventId;

    public TeamDetailPointCriteria() {}

    public TeamDetailPointCriteria(TeamDetailPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.teamPointId = other.teamPointId == null ? null : other.teamPointId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
    }

    @Override
    public TeamDetailPointCriteria copy() {
        return new TeamDetailPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getPoints() {
        return points;
    }

    public void setPoints(FloatFilter points) {
        this.points = points;
    }

    public LongFilter getTeamPointId() {
        return teamPointId;
    }

    public void setTeamPointId(LongFilter teamPointId) {
        this.teamPointId = teamPointId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TeamDetailPointCriteria that = (TeamDetailPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(teamPointId, that.teamPointId) &&
            Objects.equals(eventId, that.eventId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, teamPointId, eventId);
    }

    @Override
    public String toString() {
        return (
            "TeamDetailPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (teamPointId != null ? "teamPointId=" + teamPointId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}"
        );
    }
}
