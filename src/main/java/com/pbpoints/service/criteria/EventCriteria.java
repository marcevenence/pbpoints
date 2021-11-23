package com.pbpoints.service.criteria;

import com.pbpoints.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pbpoints.domain.Event} entity. This class is used
 * in {@link com.pbpoints.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

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

    private LocalDateFilter fromDate;

    private LocalDateFilter endDate;

    private LocalDateFilter endInscriptionDate;

    private StatusFilter status;

    private InstantFilter createDate;

    private InstantFilter updatedDate;

    private LongFilter tournamentId;

    private LongFilter fieldId;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fromDate = other.fromDate == null ? null : other.fromDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.endInscriptionDate = other.endInscriptionDate == null ? null : other.endInscriptionDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.fieldId = other.fieldId == null ? null : other.fieldId.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
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

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public LocalDateFilter fromDate() {
        if (fromDate == null) {
            fromDate = new LocalDateFilter();
        }
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            endDate = new LocalDateFilter();
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LocalDateFilter getEndInscriptionDate() {
        return endInscriptionDate;
    }

    public LocalDateFilter endInscriptionDate() {
        if (endInscriptionDate == null) {
            endInscriptionDate = new LocalDateFilter();
        }
        return endInscriptionDate;
    }

    public void setEndInscriptionDate(LocalDateFilter endInscriptionDate) {
        this.endInscriptionDate = endInscriptionDate;
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

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public InstantFilter createDate() {
        if (createDate == null) {
            createDate = new InstantFilter();
        }
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public InstantFilter getUpdatedDate() {
        return updatedDate;
    }

    public InstantFilter updatedDate() {
        if (updatedDate == null) {
            updatedDate = new InstantFilter();
        }
        return updatedDate;
    }

    public void setUpdatedDate(InstantFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public LongFilter tournamentId() {
        if (tournamentId == null) {
            tournamentId = new LongFilter();
        }
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LongFilter getFieldId() {
        return fieldId;
    }

    public LongFilter fieldId() {
        if (fieldId == null) {
            fieldId = new LongFilter();
        }
        return fieldId;
    }

    public void setFieldId(LongFilter fieldId) {
        this.fieldId = fieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(endInscriptionDate, that.endInscriptionDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(fieldId, that.fieldId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fromDate, endDate, endInscriptionDate, status, createDate, updatedDate, tournamentId, fieldId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (fromDate != null ? "fromDate=" + fromDate + ", " : "") +
            (endDate != null ? "endDate=" + endDate + ", " : "") +
            (endInscriptionDate != null ? "endInscriptionDate=" + endInscriptionDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            (fieldId != null ? "fieldId=" + fieldId + ", " : "") +
            "}";
    }
}
