package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.List;

public class RosterSubsDTO implements Serializable {

    private Long id;
    private String code;
    private String profile;
    private Long eventCategoryId;
    private RosterDTO roster;
    private List<PlayerDTO> players;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Long getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Long eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }

    public RosterDTO getRoster() {
        return roster;
    }

    public void setRoster(RosterDTO roster) {
        this.roster = roster;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return (
            "RosterSubsDTO{" +
            "id=" +
            id +
            ", code='" +
            code +
            '\'' +
            ", profile='" +
            profile +
            '\'' +
            ", eventCategoryId=" +
            eventCategoryId +
            ", roster=" +
            roster +
            ", players=" +
            players +
            '}'
        );
    }
}
