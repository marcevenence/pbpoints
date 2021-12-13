package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pbpoints.domain.Tournament} entity.
 */
@ApiModel(description = "Tournament entity.\n@author Marcelo Miño")
public class TournamentDTO implements Serializable {

    private Long id;

    private String name;

    private Integer closeInscrDays;

    private Status status;

    private Boolean categorize;

    @Lob
    private byte[] logo;

    private String logoContentType;
    private Integer cantPlayersNextCategory;

    private Integer qtyTeamGroups;

    @NotNull
    private LocalDate startSeason;

    @NotNull
    private LocalDate endSeason;

    private UserDTO owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCloseInscrDays() {
        return closeInscrDays;
    }

    public void setCloseInscrDays(Integer closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean isCategorize() {
        return categorize;
    }

    public void setCategorize(Boolean categorize) {
        this.categorize = categorize;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return logoContentType;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Integer getCantPlayersNextCategory() {
        return cantPlayersNextCategory;
    }

    public void setCantPlayersNextCategory(Integer cantPlayersNextCategory) {
        this.cantPlayersNextCategory = cantPlayersNextCategory;
    }

    public Integer getQtyTeamGroups() {
        return qtyTeamGroups;
    }

    public void setQtyTeamGroups(Integer qtyTeamGroups) {
        this.qtyTeamGroups = qtyTeamGroups;
    }

    public LocalDate getStartSeason() {
        return startSeason;
    }

    public void setStartSeason(LocalDate startSeason) {
        this.startSeason = startSeason;
    }

    public LocalDate getEndSeason() {
        return endSeason;
    }

    public void setEndSeason(LocalDate endSeason) {
        this.endSeason = endSeason;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TournamentDTO)) {
            return false;
        }

        TournamentDTO tournamentDTO = (TournamentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tournamentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TournamentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", closeInscrDays=" + getCloseInscrDays() +
            ", status='" + getStatus() + "'" +
            ", categorize='" + isCategorize() + "'" +
            ", logo=''" +
            ", cantPlayersNextCategory=" + getCantPlayersNextCategory() +
            ", qtyTeamGroups=" + getQtyTeamGroups() +
            ", startSeason='" + getStartSeason() + "'" +
            ", endSeason='" + getEndSeason() + "'" +
            ", owner=" + getOwner() +
            "}";
    }
}
