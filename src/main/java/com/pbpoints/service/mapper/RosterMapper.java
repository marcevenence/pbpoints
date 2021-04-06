package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.RosterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Roster} and its DTO {@link RosterDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, EventCategoryMapper.class })
public interface RosterMapper extends EntityMapper<RosterDTO, Roster> {
    @Mapping(target = "team", source = "team", qualifiedByName = "name")
    @Mapping(target = "eventCategory", source = "eventCategory", qualifiedByName = "id")
    RosterDTO toDto(Roster s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RosterDTO toDtoId(Roster roster);
}
