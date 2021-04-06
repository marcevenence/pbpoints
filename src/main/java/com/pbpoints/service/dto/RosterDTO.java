package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

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

    public Boolean getActive() {
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
        if (!(o instanceof RosterDTO)) {
            return false;
        }

        RosterDTO rosterDTO = (RosterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rosterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RosterDTO{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", team=" + getTeam() +
            ", eventCategory=" + getEventCategory() +
            "}";
    }
}
