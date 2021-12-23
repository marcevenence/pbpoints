package com.pbpoints.service.criteria;

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
 * Criteria class for the {@link com.pbpoints.domain.TeamCategoryPoint} entity. This class is used
 * in {@link com.pbpoints.web.rest.TeamCategoryPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /team-category-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamCategoryPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private IntegerFilter position;

    private LongFilter teamDetailPointId;

    private LongFilter eventCategoryId;

    public TeamCategoryPointCriteria() {}

    public TeamCategoryPointCriteria(TeamCategoryPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.teamDetailPointId = other.teamDetailPointId == null ? null : other.teamDetailPointId.copy();
        this.eventCategoryId = other.eventCategoryId == null ? null : other.eventCategoryId.copy();
    }

    @Override
    public TeamCategoryPointCriteria copy() {
        return new TeamCategoryPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getPoints() {
        return points;
    }

    public FloatFilter points() {
        if (points == null) {
            points = new FloatFilter();
        }
        return points;
    }

    public void setPoints(FloatFilter points) {
        this.points = points;
    }

    public IntegerFilter getPosition() {
        return position;
    }

    public IntegerFilter position() {
        if (position == null) {
            position = new IntegerFilter();
        }
        return position;
    }

    public void setPosition(IntegerFilter position) {
        this.position = position;
    }

    public LongFilter getTeamDetailPointId() {
        return teamDetailPointId;
    }

    public LongFilter teamDetailPointId() {
        if (teamDetailPointId == null) {
            teamDetailPointId = new LongFilter();
        }
        return teamDetailPointId;
    }

    public void setTeamDetailPointId(LongFilter teamDetailPointId) {
        this.teamDetailPointId = teamDetailPointId;
    }

    public LongFilter getEventCategoryId() {
        return eventCategoryId;
    }

    public LongFilter eventCategoryId() {
        if (eventCategoryId == null) {
            eventCategoryId = new LongFilter();
        }
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
        final TeamCategoryPointCriteria that = (TeamCategoryPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(position, that.position) &&
            Objects.equals(teamDetailPointId, that.teamDetailPointId) &&
            Objects.equals(eventCategoryId, that.eventCategoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, position, teamDetailPointId, eventCategoryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamCategoryPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (teamDetailPointId != null ? "teamDetailPointId=" + teamDetailPointId + ", " : "") +
            (eventCategoryId != null ? "eventCategoryId=" + eventCategoryId + ", " : "") +
            "}";
    }
}
