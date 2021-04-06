package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDetailPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link PlayerDetailPoint} and its DTO {@link PlayerDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { EventMapper.class, PlayerPointMapper.class })
public interface PlayerDetailPointMapper extends com.pbpoints.service.mapper.EntityMapper<PlayerDetailPointDTO, PlayerDetailPoint> {
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "playerPoint.id", target = "playerPointId")
    PlayerDetailPointDTO toDto(PlayerDetailPoint playerDetailPoint);

    @Mapping(source = "eventId", target = "event")
    @Mapping(source = "playerPointId", target = "playerPoint")
    PlayerDetailPoint toEntity(PlayerDetailPointDTO playerDetailPointDTO);

    default PlayerDetailPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint();
        playerDetailPoint.setId(id);
        return playerDetailPoint;
    }
}
