package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.Team} entity.
 */
@ApiModel(description = "Team entity.\n@author Marcelo Miño")
public class TeamDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean active;

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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;
        if (teamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "TeamDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", active='" +
            isActive() +
            "'" +
            ", owner=" +
            getOwner() +
            "'" +
            "}"
        );
    }
}
