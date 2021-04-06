package com.pbpoints.service.criteria;

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
 * Criteria class for the {@link com.pbpoints.domain.Tournament} entity. This class is used
 * in {@link com.pbpoints.web.rest.TournamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentCriteria implements Serializable, Criteria {

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

    private StringFilter name;

    private IntegerFilter closeInscrDays;

    private StatusFilter status;

    private BooleanFilter categorize;

    private IntegerFilter cantPlayersNextCategory;

    private IntegerFilter qtyTeamGroups;

    private LongFilter eventId;

    private LongFilter ownerId;

    public TournamentCriteria() {}

    public TournamentCriteria(TournamentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.closeInscrDays = other.closeInscrDays == null ? null : other.closeInscrDays.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.categorize = other.categorize == null ? null : other.categorize.copy();
        this.cantPlayersNextCategory = other.cantPlayersNextCategory == null ? null : other.cantPlayersNextCategory.copy();
        this.qtyTeamGroups = other.qtyTeamGroups == null ? null : other.qtyTeamGroups.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
    }

    @Override
    public TournamentCriteria copy() {
        return new TournamentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getCloseInscrDays() {
        return closeInscrDays;
    }

    public IntegerFilter closeInscrDays() {
        if (closeInscrDays == null) {
            closeInscrDays = new IntegerFilter();
        }
        return closeInscrDays;
    }

    public void setCloseInscrDays(IntegerFilter closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getCategorize() {
        return categorize;
    }

    public BooleanFilter categorize() {
        if (categorize == null) {
            categorize = new BooleanFilter();
        }
        return categorize;
    }

    public void setCategorize(BooleanFilter categorize) {
        this.categorize = categorize;
    }

    public IntegerFilter getCantPlayersNextCategory() {
        return cantPlayersNextCategory;
    }

    public IntegerFilter cantPlayersNextCategory() {
        if (cantPlayersNextCategory == null) {
            cantPlayersNextCategory = new IntegerFilter();
        }
        return cantPlayersNextCategory;
    }

    public void setCantPlayersNextCategory(IntegerFilter cantPlayersNextCategory) {
        this.cantPlayersNextCategory = cantPlayersNextCategory;
    }

    public IntegerFilter getQtyTeamGroups() {
        return qtyTeamGroups;
    }

    public IntegerFilter qtyTeamGroups() {
        if (qtyTeamGroups == null) {
            qtyTeamGroups = new IntegerFilter();
        }
        return qtyTeamGroups;
    }

    public void setQtyTeamGroups(IntegerFilter qtyTeamGroups) {
        this.qtyTeamGroups = qtyTeamGroups;
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

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public LongFilter ownerId() {
        if (ownerId == null) {
            ownerId = new LongFilter();
        }
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentCriteria that = (TournamentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(closeInscrDays, that.closeInscrDays) &&
            Objects.equals(status, that.status) &&
            Objects.equals(categorize, that.categorize) &&
            Objects.equals(cantPlayersNextCategory, that.cantPlayersNextCategory) &&
            Objects.equals(qtyTeamGroups, that.qtyTeamGroups) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(ownerId, that.ownerId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, closeInscrDays, status, categorize, cantPlayersNextCategory, qtyTeamGroups, eventId, ownerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (closeInscrDays != null ? "closeInscrDays=" + closeInscrDays + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (categorize != null ? "categorize=" + categorize + ", " : "") +
            (cantPlayersNextCategory != null ? "cantPlayersNextCategory=" + cantPlayersNextCategory + ", " : "") +
            (qtyTeamGroups != null ? "qtyTeamGroups=" + qtyTeamGroups + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            "}";
    }
}
