package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.SponsorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sponsor} and its DTO {@link SponsorDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface SponsorMapper extends EntityMapper<SponsorDTO, Sponsor> {
    @Mapping(target = "tournament", source = "tournament")
    SponsorDTO toDto(Sponsor s);
}
