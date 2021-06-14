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
 * Criteria class for the {@link com.pbpoints.domain.Formula} entity. This class is used
 * in {@link com.pbpoints.web.rest.FormulaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formulas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FormulaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter formula;

    private StringFilter var1;

    private StringFilter var2;

    private StringFilter var3;

    private StringFilter description;

    private StringFilter example;

    private LongFilter tournamentId;

    public FormulaCriteria() {}

    public FormulaCriteria(FormulaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.formula = other.formula == null ? null : other.formula.copy();
        this.var1 = other.var1 == null ? null : other.var1.copy();
        this.var2 = other.var2 == null ? null : other.var2.copy();
        this.var3 = other.var3 == null ? null : other.var3.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.example = other.example == null ? null : other.example.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
    }

    @Override
    public FormulaCriteria copy() {
        return new FormulaCriteria(this);
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

    public StringFilter getFormula() {
        return formula;
    }

    public StringFilter formula() {
        if (formula == null) {
            formula = new StringFilter();
        }
        return formula;
    }

    public void setFormula(StringFilter formula) {
        this.formula = formula;
    }

    public StringFilter getVar1() {
        return var1;
    }

    public StringFilter var1() {
        if (var1 == null) {
            var1 = new StringFilter();
        }
        return var1;
    }

    public void setVar1(StringFilter var1) {
        this.var1 = var1;
    }

    public StringFilter getVar2() {
        return var2;
    }

    public StringFilter var2() {
        if (var2 == null) {
            var2 = new StringFilter();
        }
        return var2;
    }

    public void setVar2(StringFilter var2) {
        this.var2 = var2;
    }

    public StringFilter getVar3() {
        return var3;
    }

    public StringFilter var3() {
        if (var3 == null) {
            var3 = new StringFilter();
        }
        return var3;
    }

    public void setVar3(StringFilter var3) {
        this.var3 = var3;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getExample() {
        return example;
    }

    public StringFilter example() {
        if (example == null) {
            example = new StringFilter();
        }
        return example;
    }

    public void setExample(StringFilter example) {
        this.example = example;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FormulaCriteria that = (FormulaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(formula, that.formula) &&
            Objects.equals(var1, that.var1) &&
            Objects.equals(var2, that.var2) &&
            Objects.equals(var3, that.var3) &&
            Objects.equals(description, that.description) &&
            Objects.equals(example, that.example) &&
            Objects.equals(tournamentId, that.tournamentId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, formula, var1, var2, var3, description, example, tournamentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormulaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (formula != null ? "formula=" + formula + ", " : "") +
            (var1 != null ? "var1=" + var1 + ", " : "") +
            (var2 != null ? "var2=" + var2 + ", " : "") +
            (var3 != null ? "var3=" + var3 + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (example != null ? "example=" + example + ", " : "") +
            (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
            "}";
    }
}
