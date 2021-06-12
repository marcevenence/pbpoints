package com.pbpoints.service.dto;

import com.pbpoints.domain.EventCategory;
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

    private TeamDTO team;

    private EventCategoryDTO eventCategory;

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

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public EventCategoryDTO getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategoryDTO eventCategory) {
        this.eventCategory = eventCategory;
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
            getTeam() +
            "'" +
            ", eventCategory=" +
            getEventCategory() +
            "}"
        );
    }
}
