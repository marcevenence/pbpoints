package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.FormatDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Format} and its DTO {@link FormatDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.TournamentMapper.class })
public interface FormatMapper extends com.pbpoints.service.mapper.EntityMapper<FormatDTO, Format> {
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    FormatDTO toDto(Format format);

    @Mapping(source = "tournamentId", target = "tournament")
    Format toEntity(FormatDTO formatDTO);

    default Format fromId(Long id) {
        if (id == null) {
            return null;
        }
        Format format = new Format();
        format.setId(id);
        return format;
    }
}
