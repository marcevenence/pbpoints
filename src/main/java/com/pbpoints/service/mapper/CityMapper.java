package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.CityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.ProvinceMapper.class })
public interface CityMapper extends com.pbpoints.service.mapper.EntityMapper<CityDTO, City> {
    @Mapping(source = "province", target = "province")
    CityDTO toDto(City city);

    @Mapping(source = "province", target = "province")
    City toEntity(CityDTO cityDTO);

    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}
