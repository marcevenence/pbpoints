package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerDetailPoint} and its DTO {@link PlayerDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlayerPointMapper.class, EventCategoryMapper.class })
public interface PlayerDetailPointMapper extends EntityMapper<PlayerDetailPointDTO, PlayerDetailPoint> {
    @Mapping(target = "playerPoint", source = "playerPoint")
    @Mapping(target = "eventCategory", source = "eventCategory")
    PlayerDetailPointDTO toDto(PlayerDetailPoint s);

    @Mapping(target = "playerPoint", source = "playerPoint")
    @Mapping(target = "eventCategory", source = "eventCategory")
    PlayerDetailPoint toEntity(PlayerDetailPointDTO s);
}
