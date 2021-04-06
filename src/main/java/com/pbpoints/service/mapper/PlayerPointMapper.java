package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.PlayerPointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerPoint} and its DTO {@link PlayerPointDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class, UserMapper.class, CategoryMapper.class })
public interface PlayerPointMapper extends EntityMapper<PlayerPointDTO, PlayerPoint> {
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "category", source = "category", qualifiedByName = "name")
    PlayerPointDTO toDto(PlayerPoint s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerPointDTO toDtoId(PlayerPoint playerPoint);
}
