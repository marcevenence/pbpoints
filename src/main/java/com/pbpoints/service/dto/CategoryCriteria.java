package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.TimeType;
import com.pbpoints.domain.enumeration.TimeType;
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
 * Criteria class for the {@link com.pbpoints.domain.Category} entity. This class is used
 * in {@link com.pbpoints.web.rest.CategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TimeType
     */
    public static class TimeTypeFilter extends Filter<TimeType> {

        public TimeTypeFilter() {}

        public TimeTypeFilter(TimeTypeFilter filter) {
            super(filter);
        }

        @Override
        public TimeTypeFilter copy() {
            return new TimeTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private TimeTypeFilter gameTimeType;

    private IntegerFilter gameTime;

    private TimeTypeFilter stopTimeType;

    private IntegerFilter stopTime;

    private IntegerFilter totalPoints;

    private IntegerFilter difPoints;

    private IntegerFilter order;

    private LongFilter tournamentId;

    public CategoryCriteria() {}

    public CategoryCriteria(CategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.gameTimeType = other.gameTimeType == null ? null : other.gameTimeType.copy();
        this.gameTime = other.gameTime == null ? null : other.gameTime.copy();
        this.stopTimeType = other.stopTimeType == null ? null : other.stopTimeType.copy();
        this.stopTime = other.stopTime == null ? null : other.stopTime.copy();
        this.totalPoints = other.totalPoints == null ? null : other.totalPoints.copy();
        this.difPoints = other.difPoints == null ? null : other.difPoints.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
    }

    @Override
    public CategoryCriteria copy() {
        return new CategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public TimeTypeFilter getGameTimeType() {
        return gameTimeType;
    }

    public void setGameTimeType(TimeTypeFilter gameTimeType) {
        this.gameTimeType = gameTimeType;
    }

    public IntegerFilter getGameTime() {
        return gameTime;
    }

    public void setGameTime(IntegerFilter gameTime) {
        this.gameTime = gameTime;
    }

    public TimeTypeFilter getStopTimeType() {
        return stopTimeType;
    }

    public void setStopTimeType(TimeTypeFilter stopTimeType) {
        this.stopTimeType = stopTimeType;
    }

    public IntegerFilter getStopTime() {
        return stopTime;
    }

    public void setStopTime(IntegerFilter stopTime) {
        this.stopTime = stopTime;
    }

    public IntegerFilter getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(IntegerFilter totalPoints) {
        this.totalPoints = totalPoints;
    }

    public IntegerFilter getDifPoints() {
        return difPoints;
    }

    public void setDifPoints(IntegerFilter difPoints) {
        this.difPoints = difPoints;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CategoryCriteria that = (CategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(gameTimeType, that.gameTimeType) &&
            Objects.equals(gameTime, that.gameTime) &&
            Objects.equals(stopTimeType, that.stopTimeType) &&
            Objects.equals(stopTime, that.stopTime) &&
            Objects.equals(totalPoints, that.totalPoints) &&
            Objects.equals(difPoints, that.difPoints) &&
            Objects.equals(order, that.order) &&
            Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            gameTimeType,
            gameTime,
            stopTimeType,
            stopTime,
            totalPoints,
            difPoints,
            order,
            tournamentId
        );
    }

    @Override
    public String toString() {
        return (
            "CategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (gameTimeType != null ? "gameTimeType=" + gameTimeType + ", " : "") +
            (gameTime != null ? "gameTime=" + gameTime + ", " : "") +
            (stopTimeType != null ? "stopTimeType=" + stopTimeType + ", " : "") +
            (stopTime != null ? "stopTime=" + stopTime + ", " : "") +
            (totalPoints != null ? "totalPoints=" + totalPoints + ", " : "") +
            (difPoints != null ? "difPoints=" + difPoints + ", " : "") +
            (order != null ? "order=" + order + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            "}"
        );
    }
}
