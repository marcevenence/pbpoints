package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.EventCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link EventCategory} and its DTO {@link EventCategoryDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        com.pbpoints.service.mapper.EventMapper.class,
        com.pbpoints.service.mapper.CategoryMapper.class,
        com.pbpoints.service.mapper.FormatMapper.class,
    }
)
public interface EventCategoryMapper extends com.pbpoints.service.mapper.EntityMapper<EventCategoryDTO, EventCategory> {
    @Mapping(source = "event", target = "event")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "format", target = "format")
    EventCategoryDTO toDto(EventCategory eventCategory);

    @Mapping(source = "event", target = "event")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "format", target = "format")
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "removeGame", ignore = true)
    @Mapping(target = "rosters", ignore = true)
    @Mapping(target = "removeRoster", ignore = true)
    EventCategory toEntity(EventCategoryDTO eventCategoryDTO);

    default EventCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventCategory eventCategory = new EventCategory();
        eventCategory.setId(id);
        return eventCategory;
    }
}
