package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.Status;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

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

    private Long ownerId;

    private String ownerLogin;

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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TournamentDTO tournamentDTO = (TournamentDTO) o;
        if (tournamentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tournamentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "TournamentDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", closeInscrDays=" +
            getCloseInscrDays() +
            ", status='" +
            getStatus() +
            "'" +
            ", categorize='" +
            isCategorize() +
            "'" +
            ", logo='" +
            getLogo() +
            "'" +
            ", cantPlayersNextCategory=" +
            getCantPlayersNextCategory() +
            ", qtyTeamGroups=" +
            getQtyTeamGroups() +
            ", ownerId=" +
            getOwnerId() +
            ", ownerLogin='" +
            getOwnerLogin() +
            "'" +
            "}"
        );
    }
}
