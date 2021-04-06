package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.EventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = { CityMapper.class, TournamentMapper.class })
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "city", source = "city", qualifiedByName = "id")
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "id")
    EventDTO toDto(Event s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EventDTO toDtoName(Event event);
}
