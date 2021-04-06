package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.Roster} entity.
 */
@ApiModel(description = "Roster entity.\n@author Marcelo Miño")
public class RosterDTO implements Serializable {

    private Long id;

    private Boolean active;

    private Long teamId;

    private String teamName;

    private Long eventCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Long eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RosterDTO rosterDTO = (RosterDTO) o;
        if (rosterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rosterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "RosterDTO{" +
            "id=" +
            getId() +
            ", active='" +
            isActive() +
            "'" +
            ", team=" +
            getTeamId() +
            ", team='" +
            getTeamName() +
            "'" +
            ", eventCategory=" +
            getEventCategoryId() +
            "}"
        );
    }
}
