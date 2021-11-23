package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.TournamentGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TournamentGroup} and its DTO {@link TournamentGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface TournamentGroupMapper extends EntityMapper<TournamentGroupDTO, TournamentGroup> {
    @Mapping(target = "tournamentA", source = "tournamentA")
    @Mapping(target = "tournamentB", source = "tournamentB")
    TournamentGroupDTO toDto(TournamentGroup s);
}
