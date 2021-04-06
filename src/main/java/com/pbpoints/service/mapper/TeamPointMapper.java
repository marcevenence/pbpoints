package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TeamPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamPoint} and its DTO {@link TeamPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, TournamentMapper.class })
public interface TeamPointMapper extends EntityMapper<TeamPointDTO, TeamPoint> {
    @Mapping(target = "team", source = "team", qualifiedByName = "name")
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    TeamPointDTO toDto(TeamPoint s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TeamPointDTO toDtoId(TeamPoint teamPoint);
}
