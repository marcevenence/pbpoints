package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.GameDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, EventCategoryMapper.class })
public interface GameMapper extends EntityMapper<GameDTO, Game> {
    @Mapping(target = "teamA", source = "teamA")
    @Mapping(target = "teamB", source = "teamB")
    @Mapping(target = "eventCategory", source = "eventCategory")
    GameDTO toDto(Game s);
}
