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
 * Criteria class for the {@link com.pbpoints.domain.PlayerPointHistory} entity. This class is used
 * in {@link com.pbpoints.web.rest.PlayerPointHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /player-point-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerPointHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private LongFilter playerPointId;

    private LongFilter categoryId;

    private LongFilter seasonId;

    private Boolean distinct;

    public PlayerPointHistoryCriteria() {}

    public PlayerPointHistoryCriteria(PlayerPointHistoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.playerPointId = other.playerPointId == null ? null : other.playerPointId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.seasonId = other.seasonId == null ? null : other.seasonId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PlayerPointHistoryCriteria copy() {
        return new PlayerPointHistoryCriteria(this);
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getSeasonId() {
        return seasonId;
    }

    public LongFilter seasonId() {
        if (seasonId == null) {
            seasonId = new LongFilter();
        }
        return seasonId;
    }

    public void setSeasonId(LongFilter seasonId) {
        this.seasonId = seasonId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerPointHistoryCriteria that = (PlayerPointHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(playerPointId, that.playerPointId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(seasonId, that.seasonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, points, playerPointId, categoryId, seasonId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerPointHistoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (playerPointId != null ? "playerPointId=" + playerPointId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (seasonId != null ? "seasonId=" + seasonId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
