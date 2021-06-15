package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, RosterMapper.class, CategoryMapper.class })
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "roster", source = "roster")
    @Mapping(target = "category", source = "category")
    PlayerDTO toDto(Player s);
}
