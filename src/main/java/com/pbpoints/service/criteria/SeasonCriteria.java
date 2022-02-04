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
 * Criteria class for the {@link com.pbpoints.domain.Season} entity. This class is used
 * in {@link com.pbpoints.web.rest.SeasonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seasons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SeasonCriteria implements Serializable, Criteria {

    /**
     * Class for filtering status
     */
    public static class statusFilter extends Filter<Status> {

        public statusFilter() {}

        public statusFilter(statusFilter filter) {
            super(filter);
        }

        @Override
        public statusFilter copy() {
            return new statusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter anio;

    private statusFilter status;

    private LongFilter tournamentId;

    private Boolean distinct;

    public SeasonCriteria() {}

    public SeasonCriteria(SeasonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.anio = other.anio == null ? null : other.anio.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SeasonCriteria copy() {
        return new SeasonCriteria(this);
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

    public IntegerFilter getAnio() {
        return anio;
    }

    public IntegerFilter anio() {
        if (anio == null) {
            anio = new IntegerFilter();
        }
        return anio;
    }

    public void setAnio(IntegerFilter anio) {
        this.anio = anio;
    }

    public statusFilter getStatus() {
        return status;
    }

    public statusFilter status() {
        if (status == null) {
            status = new statusFilter();
        }
        return status;
    }

    public void setStatus(statusFilter status) {
        this.status = status;
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
        final SeasonCriteria that = (SeasonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(anio, that.anio) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, anio, status, tournamentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeasonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (anio != null ? "anio=" + anio + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
