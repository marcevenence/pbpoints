package com.pbpoints.service.dto;

import java.util.List;

public class RosterWithPlayersDTO {

    private List<PlayerDTO> players;

    private TeamDTO team;

    private EventCategoryDTO eventCategory;

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
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
}
