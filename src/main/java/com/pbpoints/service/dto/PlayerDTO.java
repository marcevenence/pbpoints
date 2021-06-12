package com.pbpoints.service.dto;

import com.pbpoints.domain.enumeration.ProfileUser;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pbpoints.domain.Player} entity.
 */
public class PlayerDTO implements Serializable {

    private Long id;

    private ProfileUser profile;

    private UserDTO user;

    private RosterDTO roster;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileUser getProfile() {
        return profile;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public RosterDTO getRoster() {
        return roster;
    }

    public void setRoster(RosterDTO roster) {
        this.roster = roster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if (playerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return (
            "PlayerDTO{" +
            "id=" +
            getId() +
            ", profile='" +
            getProfile() +
            "'" +
            ", user=" +
            getUser() +
            "'" +
            ", roster=" +
            getRoster() +
            "}"
        );
    }
}
