package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.ProvinceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class })
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    ProvinceDTO toDto(Province s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoId(Province province);
}
