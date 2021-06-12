package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link PlayerPoint} and its DTO {@link PlayerPointDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { com.pbpoints.service.mapper.TournamentMapper.class, UserMapper.class, com.pbpoints.service.mapper.CategoryMapper.class }
)
public interface PlayerPointMapper extends com.pbpoints.service.mapper.EntityMapper<PlayerPointDTO, PlayerPoint> {
    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "category", target = "category")
    PlayerPointDTO toDto(PlayerPoint playerPoint);

    @Mapping(source = "tournament", target = "tournament")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "category", target = "category")
    PlayerPoint toEntity(PlayerPointDTO playerPointDTO);

    default PlayerPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlayerPoint playerPoint = new PlayerPoint();
        playerPoint.setId(id);
        return playerPoint;
    }
}
