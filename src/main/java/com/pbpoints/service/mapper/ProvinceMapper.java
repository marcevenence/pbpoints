package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.ProvinceDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.CountryMapper.class })
public interface ProvinceMapper extends com.pbpoints.service.mapper.EntityMapper<ProvinceDTO, Province> {
    @Mapping(source = "country", target = "country")
    ProvinceDTO toDto(Province province);

    @Mapping(source = "country", target = "country")
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "removeCity", ignore = true)
    Province toEntity(ProvinceDTO provinceDTO);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProvinceDTO toDtoId(Province province);

    default Province fromId(Long id) {
        if (id == null) {
            return null;
        }
        Province province = new Province();
        province.setId(id);
        return province;
    }
}
