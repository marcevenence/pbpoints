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
 * Criteria class for the {@link com.pbpoints.domain.Roster} entity. This class is used
 * in {@link com.pbpoints.web.rest.RosterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rosters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RosterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter active;

    private LongFilter playerId;

    private LongFilter teamId;

    private LongFilter eventCategoryId;

    public RosterCriteria() {}

    public RosterCriteria(RosterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.playerId = other.playerId == null ? null : other.playerId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.eventCategoryId = other.eventCategoryId == null ? null : other.eventCategoryId.copy();
    }

    @Override
    public RosterCriteria copy() {
        return new RosterCriteria(this);
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

    public BooleanFilter getActive() {
        return active;
    }

    public BooleanFilter active() {
        if (active == null) {
            active = new BooleanFilter();
        }
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getPlayerId() {
        return playerId;
    }

    public LongFilter playerId() {
        if (playerId == null) {
            playerId = new LongFilter();
        }
        return playerId;
    }

    public void setPlayerId(LongFilter playerId) {
        this.playerId = playerId;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public LongFilter teamId() {
        if (teamId == null) {
            teamId = new LongFilter();
        }
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
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
        final RosterCriteria that = (RosterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(active, that.active) &&
            Objects.equals(playerId, that.playerId) &&
            Objects.equals(teamId, that.teamId) &&
            Objects.equals(eventCategoryId, that.eventCategoryId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, active, playerId, teamId, eventCategoryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RosterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (active != null ? "active=" + active + ", " : "") +
            (playerId != null ? "playerId=" + playerId + ", " : "") +
            (teamId != null ? "teamId=" + teamId + ", " : "") +
            (eventCategoryId != null ? "eventCategoryId=" + eventCategoryId + ", " : "") +
            "}";
    }
}
