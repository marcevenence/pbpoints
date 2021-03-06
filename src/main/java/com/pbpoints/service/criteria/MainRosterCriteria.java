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
 * Criteria class for the {@link com.pbpoints.domain.MainRoster} entity. This class is used
 * in {@link com.pbpoints.web.rest.MainRosterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /main-rosters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MainRosterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter teamId;

    private LongFilter userExtraId;

    public MainRosterCriteria() {}

    public MainRosterCriteria(MainRosterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.userExtraId = other.userExtraId == null ? null : other.userExtraId.copy();
    }

    @Override
    public MainRosterCriteria copy() {
        return new MainRosterCriteria(this);
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

    public LongFilter getUserExtraId() {
        return userExtraId;
    }

    public LongFilter userExtraId() {
        if (userExtraId == null) {
            userExtraId = new LongFilter();
        }
        return userExtraId;
    }

    public void setUserExtraId(LongFilter userExtraId) {
        this.userExtraId = userExtraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MainRosterCriteria that = (MainRosterCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(teamId, that.teamId) && Objects.equals(userExtraId, that.userExtraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamId, userExtraId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MainRosterCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (teamId != null ? "teamId=" + teamId + ", " : "") +
            (userExtraId != null ? "userExtraId=" + userExtraId + ", " : "") +
            "}";
    }
}
