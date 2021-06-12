package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.GameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.TeamMapper.class, EventCategoryMapper.class })
public interface GameMapper extends com.pbpoints.service.mapper.EntityMapper<GameDTO, Game> {
    @Mapping(source = "teamA", target = "teamA")
    @Mapping(source = "teamB", target = "teamB")
    @Mapping(source = "eventCategory", target = "eventCategory")
    GameDTO toDto(Game game);

    @Mapping(source = "teamA", target = "teamA")
    @Mapping(source = "teamB", target = "teamB")
    @Mapping(source = "eventCategory", target = "eventCategory")
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
