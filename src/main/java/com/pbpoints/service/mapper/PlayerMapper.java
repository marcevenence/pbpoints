package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, RosterMapper.class, CategoryMapper.class })
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "roster", source = "roster", qualifiedByName = "id")
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    PlayerDTO toDto(Player s);
}
