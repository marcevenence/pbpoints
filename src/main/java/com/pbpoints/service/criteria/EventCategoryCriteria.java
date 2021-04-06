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

    private LongFilter gameId;

    private LongFilter rosterId;

    private LongFilter eventId;

    private LongFilter categoryId;

    private LongFilter formatId;

    public EventCategoryCriteria() {}

    public EventCategoryCriteria(EventCategoryCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.splitDeck = other.splitDeck == null ? null : other.splitDeck.copy();
        this.gameId = other.gameId == null ? null : other.gameId.copy();
        this.rosterId = other.rosterId == null ? null : other.rosterId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.formatId = other.formatId == null ? null : other.formatId.copy();
    }

    @Override
    public EventCategoryCriteria copy() {
        return new EventCategoryCriteria(this);
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

    public BooleanFilter getSplitDeck() {
        return splitDeck;
    }

    public BooleanFilter splitDeck() {
        if (splitDeck == null) {
            splitDeck = new BooleanFilter();
        }
        return splitDeck;
    }

    public void setSplitDeck(BooleanFilter splitDeck) {
        this.splitDeck = splitDeck;
    }

    public LongFilter getGameId() {
        return gameId;
    }

    public LongFilter gameId() {
        if (gameId == null) {
            gameId = new LongFilter();
        }
        return gameId;
    }

    public void setGameId(LongFilter gameId) {
        this.gameId = gameId;
    }

    public LongFilter getRosterId() {
        return rosterId;
    }

    public LongFilter rosterId() {
        if (rosterId == null) {
            rosterId = new LongFilter();
        }
        return rosterId;
    }

    public void setRosterId(LongFilter rosterId) {
        this.rosterId = rosterId;
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

    public LongFilter getFormatId() {
        return formatId;
    }

    public LongFilter formatId() {
        if (formatId == null) {
            formatId = new LongFilter();
        }
        return formatId;
    }

    public void setFormatId(LongFilter formatId) {
        this.formatId = formatId;
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
            Objects.equals(gameId, that.gameId) &&
            Objects.equals(rosterId, that.rosterId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(formatId, that.formatId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, splitDeck, gameId, rosterId, eventId, categoryId, formatId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCategoryCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (splitDeck != null ? "splitDeck=" + splitDeck + ", " : "") +
            (gameId != null ? "gameId=" + gameId + ", " : "") +
            (rosterId != null ? "rosterId=" + rosterId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (formatId != null ? "formatId=" + formatId + ", " : "") +
            "}";
    }
}
