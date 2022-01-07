package com.pbpoints.service.dto;

import java.io.Serializable;
import java.util.List;

public class RosterSubsPlDTO implements Serializable {

    private PlayerDTO player;
    private List<PlayerDTO> players;

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "RosterSubsPlDTO{" + "player=" + player + ", players=" + players + '}';
    }
}
