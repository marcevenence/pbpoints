package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProvinceMapper.class })
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    CityDTO toDto(City s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoId(City city);
}
