package com.pbpoints.service.mapper;

import com.pbpoints.domain.Season;
import com.pbpoints.service.dto.SeasonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Season} and its DTO {@link SeasonDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface SeasonMapper extends EntityMapper<SeasonDTO, Season> {
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    SeasonDTO toDto(Season s);
}
