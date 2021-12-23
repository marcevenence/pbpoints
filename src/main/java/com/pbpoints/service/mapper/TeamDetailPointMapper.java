package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TeamDetailPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamDetailPoint} and its DTO {@link TeamDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamPointMapper.class, EventMapper.class })
public interface TeamDetailPointMapper extends EntityMapper<TeamDetailPointDTO, TeamDetailPoint> {
    @Mapping(target = "teamPoint", source = "teamPoint")
    @Mapping(target = "event", source = "event")
    TeamDetailPointDTO toDto(TeamDetailPoint s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeamDetailPointDTO toDtoId(TeamDetailPoint teamDetailPoint);
}
