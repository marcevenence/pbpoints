package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.EventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.TournamentMapper.class, CityMapper.class })
public interface EventMapper extends com.pbpoints.service.mapper.EntityMapper<EventDTO, Event> {
    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "city", target = "city")
    EventDTO toDto(Event event);

    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "city", target = "city")
    Event toEntity(EventDTO eventDTO);

    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
