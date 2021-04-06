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
 * Criteria class for the {@link com.pbpoints.domain.Format} entity. This class is used
 * in {@link com.pbpoints.web.rest.FormatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private FloatFilter coeficient;

    private IntegerFilter playersQty;

    private LongFilter tournamentId;

    public FormatCriteria() {}

    public FormatCriteria(FormatCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.coeficient = other.coeficient == null ? null : other.coeficient.copy();
        this.playersQty = other.playersQty == null ? null : other.playersQty.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
    }

    @Override
    public FormatCriteria copy() {
        return new FormatCriteria(this);
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

    public FloatFilter getCoeficient() {
        return coeficient;
    }

    public void setCoeficient(FloatFilter coeficient) {
        this.coeficient = coeficient;
    }

    public IntegerFilter getPlayersQty() {
        return playersQty;
    }

    public void setPlayersQty(IntegerFilter playersQty) {
        this.playersQty = playersQty;
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
        final FormatCriteria that = (FormatCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(coeficient, that.coeficient) &&
            Objects.equals(playersQty, that.playersQty) &&
            Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, coeficient, playersQty, tournamentId);
    }

    @Override
    public String toString() {
        return (
            "FormatCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (coeficient != null ? "coeficient=" + coeficient + ", " : "") +
            (playersQty != null ? "playersQty=" + playersQty + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            "}"
        );
    }
}
