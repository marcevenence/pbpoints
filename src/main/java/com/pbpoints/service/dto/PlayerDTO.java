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

    private Long userId;

    private String userLogin;

    private Long rosterId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getRosterId() {
        return rosterId;
    }

    public void setRosterId(Long rosterId) {
        this.rosterId = rosterId;
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
            getUserId() +
            ", user='" +
            getUserLogin() +
            "'" +
            ", roster=" +
            getRosterId() +
            "}"
        );
    }
}
