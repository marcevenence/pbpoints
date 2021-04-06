package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerDetailPoint} and its DTO {@link PlayerDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { EventMapper.class, PlayerPointMapper.class })
public interface PlayerDetailPointMapper extends EntityMapper<PlayerDetailPointDTO, PlayerDetailPoint> {
    @Mapping(target = "event", source = "event", qualifiedByName = "name")
    @Mapping(target = "playerPoint", source = "playerPoint", qualifiedByName = "id")
    PlayerDetailPointDTO toDto(PlayerDetailPoint s);
}
