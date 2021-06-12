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
    @Mapping(source = "event", target = "event")
    @Mapping(source = "playerPoint", target = "playerPoint")
    PlayerDetailPointDTO toDto(PlayerDetailPoint playerDetailPoint);

    @Mapping(source = "event", target = "event")
    @Mapping(source = "playerPoint", target = "playerPoint")
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
