package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.MainRoster} entity.
 */
public class MainRosterDTO implements Serializable {

    private Long id;

    private TeamDTO team;

    private UserExtraDTO userExtra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public UserExtraDTO getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(UserExtraDTO userExtra) {
        this.userExtra = userExtra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MainRosterDTO)) {
            return false;
        }

        MainRosterDTO mainRosterDTO = (MainRosterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mainRosterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MainRosterDTO{" +
            "id=" + getId() +
            ", team=" + getTeam() +
            ", userExtra=" + getUserExtra() +
            "}";
    }
}
