package com.pbpoints.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.pbpoints.domain.Team} entity.
 */
@ApiModel(description = "Team entity.\n@author Marcelo Miño")
public class TeamWithRostersDTO implements Serializable {

    private TeamDTO team;

    private List<MainRosterDTO> mainRosters;

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public List<MainRosterDTO> getMainRosters() {
        return mainRosters;
    }

    public void setMainRoster(List<MainRosterDTO> mainRoster) {
        this.mainRosters = mainRoster;
    }
}
