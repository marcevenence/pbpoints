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
 * Criteria class for the {@link com.pbpoints.domain.TournamentGroup} entity. This class is used
 * in {@link com.pbpoints.web.rest.TournamentGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournament-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentGroupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter tournamentAId;

    private LongFilter tournamentBId;

    public TournamentGroupCriteria() {}

    public TournamentGroupCriteria(TournamentGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.tournamentAId = other.tournamentAId == null ? null : other.tournamentAId.copy();
        this.tournamentBId = other.tournamentBId == null ? null : other.tournamentBId.copy();
    }

    @Override
    public TournamentGroupCriteria copy() {
        return new TournamentGroupCriteria(this);
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

    public LongFilter getTournamentAId() {
        return tournamentAId;
    }

    public LongFilter tournamentAId() {
        if (tournamentAId == null) {
            tournamentAId = new LongFilter();
        }
        return tournamentAId;
    }

    public void setTournamentAId(LongFilter tournamentAId) {
        this.tournamentAId = tournamentAId;
    }

    public LongFilter getTournamentBId() {
        return tournamentBId;
    }

    public LongFilter tournamentBId() {
        if (tournamentBId == null) {
            tournamentBId = new LongFilter();
        }
        return tournamentBId;
    }

    public void setTournamentBId(LongFilter tournamentBId) {
        this.tournamentBId = tournamentBId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentGroupCriteria that = (TournamentGroupCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(tournamentAId, that.tournamentAId) &&
            Objects.equals(tournamentBId, that.tournamentBId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tournamentAId, tournamentBId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentGroupCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (tournamentAId != null ? "tournamentAId=" + tournamentAId + ", " : "") +
            (tournamentBId != null ? "tournamentBId=" + tournamentBId + ", " : "") +
            "}";
    }
}
