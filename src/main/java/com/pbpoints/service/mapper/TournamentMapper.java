package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TournamentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tournament} and its DTO {@link TournamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TournamentMapper extends EntityMapper<TournamentDTO, Tournament> {
    @Mapping(target = "owner", source = "owner")
    TournamentDTO toDto(Tournament s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TournamentDTO toDtoId(Tournament tournament);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    @Mapping(source = "owner", target = "owner")
    Tournament toEntity(TournamentDTO tournamentDTO);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TournamentDTO toDtoName(Tournament tournament);

    default Tournament fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tournament tournament = new Tournament();
        tournament.setId(id);
        return tournament;
    }
}
