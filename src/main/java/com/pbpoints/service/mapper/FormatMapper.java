package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.FormatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Format} and its DTO {@link FormatDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface FormatMapper extends EntityMapper<FormatDTO, Format> {
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    FormatDTO toDto(Format s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FormatDTO toDtoName(Format format);
}
