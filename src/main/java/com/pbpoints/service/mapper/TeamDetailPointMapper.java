package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TeamDetailPoint} and its DTO {@link TeamDetailPointDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { com.pbpoints.service.mapper.TeamPointMapper.class, com.pbpoints.service.mapper.EventMapper.class }
)
public interface TeamDetailPointMapper extends com.pbpoints.service.mapper.EntityMapper<TeamDetailPointDTO, TeamDetailPoint> {
    @Mapping(source = "teamPoint.id", target = "teamPointId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    TeamDetailPointDTO toDto(TeamDetailPoint teamDetailPoint);

    @Mapping(source = "teamPointId", target = "teamPoint")
    @Mapping(source = "eventId", target = "event")
    TeamDetailPoint toEntity(TeamDetailPointDTO teamDetailPointDTO);

    default TeamDetailPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint();
        teamDetailPoint.setId(id);
        return teamDetailPoint;
    }
}
