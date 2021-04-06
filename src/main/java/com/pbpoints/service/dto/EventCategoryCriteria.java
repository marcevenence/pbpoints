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
 * Criteria class for the {@link com.pbpoints.domain.EventCategory} entity. This class is used
 * in {@link com.pbpoints.web.rest.EventCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter splitDeck;

    private LongFilter eventId;

    private LongFilter categoryId;

    private LongFilter formatId;

    private LongFilter gameId;

    public EventCategoryCriteria() {}

    public EventCategoryCriteria(EventCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.splitDeck = other.splitDeck == null ? null : other.splitDeck.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.formatId = other.formatId == null ? null : other.formatId.copy();
        this.gameId = other.gameId == null ? null : other.gameId.copy();
    }

    @Override
    public EventCategoryCriteria copy() {
        return new EventCategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getSplitDeck() {
        return splitDeck;
    }

    public void setSplitDeck(BooleanFilter splitDeck) {
        this.splitDeck = splitDeck;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getFormatId() {
        return formatId;
    }

    public void setFormatId(LongFilter formatId) {
        this.formatId = formatId;
    }

    public LongFilter getGameId() {
        return gameId;
    }

    public void setGameId(LongFilter gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCategoryCriteria that = (EventCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(splitDeck, that.splitDeck) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(formatId, that.formatId) &&
            Objects.equals(gameId, that.gameId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, splitDeck, eventId, categoryId, formatId, gameId);
    }

    @Override
    public String toString() {
        return (
            "EventCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (splitDeck != null ? "splitDeck=" + splitDeck + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (formatId != null ? "formatId=" + formatId + ", " : "") +
            (gameId != null ? "gameId=" + gameId + ", " : "") +
            "}"
        );
    }
}
