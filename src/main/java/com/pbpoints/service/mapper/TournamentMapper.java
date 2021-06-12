package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TournamentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Tournament} and its DTO {@link TournamentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TournamentMapper extends com.pbpoints.service.mapper.EntityMapper<TournamentDTO, Tournament> {
    @Mapping(source = "owner", target = "owner")
    TournamentDTO toDto(Tournament tournament);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    @Mapping(source = "owner", target = "owner")
    Tournament toEntity(TournamentDTO tournamentDTO);

    default Tournament fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tournament tournament = new Tournament();
        tournament.setId(id);
        return tournament;
    }
}
