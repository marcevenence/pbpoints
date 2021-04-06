package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.EventCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCategory} and its DTO {@link EventCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { EventMapper.class, CategoryMapper.class, FormatMapper.class })
public interface EventCategoryMapper extends EntityMapper<EventCategoryDTO, EventCategory> {
    @Mapping(target = "event", source = "event", qualifiedByName = "name")
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    @Mapping(target = "format", source = "format", qualifiedByName = "name")
    EventCategoryDTO toDto(EventCategory s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventCategoryDTO toDtoId(EventCategory eventCategory);
}
