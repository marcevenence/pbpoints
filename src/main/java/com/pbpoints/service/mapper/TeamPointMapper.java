package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TeamPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TeamPoint} and its DTO {@link TeamPointDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { com.pbpoints.service.mapper.TeamMapper.class, com.pbpoints.service.mapper.TournamentMapper.class }
)
public interface TeamPointMapper extends com.pbpoints.service.mapper.EntityMapper<TeamPointDTO, TeamPoint> {
    @Mapping(source = "team", target = "team")
    @Mapping(source = "tournament", target = "tournament")
    TeamPointDTO toDto(TeamPoint teamPoint);

    @Mapping(source = "team", target = "team")
    @Mapping(source = "tournament", target = "tournament")
    TeamPoint toEntity(TeamPointDTO teamPointDTO);

    default TeamPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        TeamPoint teamPoint = new TeamPoint();
        teamPoint.setId(id);
        return teamPoint;
    }
}
