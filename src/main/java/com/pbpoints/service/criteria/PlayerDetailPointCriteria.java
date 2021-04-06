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
 * Criteria class for the {@link com.pbpoints.domain.PlayerDetailPoint} entity. This class is used
 * in {@link com.pbpoints.web.rest.PlayerDetailPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /player-detail-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerDetailPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private LongFilter eventId;

    private LongFilter playerPointId;

    public PlayerDetailPointCriteria() {}

    public PlayerDetailPointCriteria(PlayerDetailPointCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.playerPointId = other.playerPointId == null ? null : other.playerPointId.copy();
    }

    @Override
    public PlayerDetailPointCriteria copy() {
        return new PlayerDetailPointCriteria(this);
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

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getPlayerPointId() {
        return playerPointId;
    }

    public LongFilter playerPointId() {
        if (playerPointId == null) {
            playerPointId = new LongFilter();
        }
        return playerPointId;
    }

    public void setPlayerPointId(LongFilter playerPointId) {
        this.playerPointId = playerPointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerDetailPointCriteria that = (PlayerDetailPointCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(playerPointId, that.playerPointId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, eventId, playerPointId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerDetailPointCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (playerPointId != null ? "playerPointId=" + playerPointId + ", " : "") +
            "}";
    }
}
