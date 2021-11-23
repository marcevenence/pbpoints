package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.FieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Field} and its DTO {@link FieldDTO}.
 */
@Mapper(componentModel = "spring", uses = { CityMapper.class })
public interface FieldMapper extends EntityMapper<FieldDTO, Field> {
    @Mapping(target = "city", source = "city")
    FieldDTO toDto(Field s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FieldDTO toDtoName(Field field);
}
