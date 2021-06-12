package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Formula} entity.
 */
public class FormulaDTO implements Serializable {

    private Long id;

    @NotNull
    private String formula;

    @NotNull
    private String var1;

    private String var2;

    private String var3;

    @NotNull
    private String description;

    private String example;

    private TournamentDTO tournament;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormulaDTO formulaDTO = (FormulaDTO) o;
        if (formulaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formulaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "FormulaDTO{" +
            "id=" +
            getId() +
            ", formula='" +
            getFormula() +
            "'" +
            ", var1='" +
            getVar1() +
            "'" +
            ", var2='" +
            getVar2() +
            "'" +
            ", var3='" +
            getVar3() +
            "'" +
            ", description='" +
            getDescription() +
            "'" +
            ", example='" +
            getExample() +
            "'" +
            ", tournament=" +
            getTournament() +
            "'" +
            "}"
        );
    }
}
