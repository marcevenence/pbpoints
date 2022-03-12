package com.pbpoints.service.dto;

import java.util.List;

public class RosterWithPlayersDTO {

    private List<PlayerDTO> players;

    private Long teamId;

    private Long eventCategoryId;

    public RosterWithPlayersDTO(List<PlayerDTO> players, Long teamId, Long eventCategoryId) {
        this.players = players;
        this.teamId = teamId;
        this.eventCategoryId = eventCategoryId;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Long eventCategory) {
        this.eventCategoryId = eventCategoryId;
    }

    @Override
    public String toString() {
        return "RosterWithPlayersDTO{" + "players=" + players + ", teamId=" + teamId + ", eventCategoryId=" + eventCategoryId + '}';
    }
}
