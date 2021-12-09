package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProvinceMapper.class })
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Mapping(target = "province", source = "province")
    LocationDTO toDto(Location s);
}
