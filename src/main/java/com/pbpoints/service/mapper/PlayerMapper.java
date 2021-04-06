package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, com.pbpoints.service.mapper.RosterMapper.class })
public interface PlayerMapper extends com.pbpoints.service.mapper.EntityMapper<PlayerDTO, Player> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "roster.id", target = "rosterId")
    PlayerDTO toDto(Player player);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "rosterId", target = "roster")
    Player toEntity(PlayerDTO playerDTO);

    default Player fromId(Long id) {
        if (id == null) {
            return null;
        }
        Player player = new Player();
        player.setId(id);
        return player;
    }
}
