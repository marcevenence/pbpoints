package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TeamCategoryPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamCategoryPoint} and its DTO {@link TeamCategoryPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamDetailPointMapper.class, EventCategoryMapper.class })
public interface TeamCategoryPointMapper extends EntityMapper<TeamCategoryPointDTO, TeamCategoryPoint> {
    @Mapping(target = "teamDetailPoint", source = "teamDetailPoint")
    @Mapping(target = "eventCategory", source = "eventCategory")
    TeamCategoryPointDTO toDto(TeamCategoryPoint s);
}
