package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.RosterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Roster} and its DTO {@link RosterDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.TeamMapper.class, EventCategoryMapper.class })
public interface RosterMapper extends com.pbpoints.service.mapper.EntityMapper<RosterDTO, Roster> {
    @Mapping(source = "team", target = "team")
    @Mapping(source = "eventCategory", target = "eventCategory")
    RosterDTO toDto(Roster roster);

    @Mapping(source = "team", target = "team")
    @Mapping(source = "eventCategory", target = "eventCategory")
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "removePlayer", ignore = true)
    Roster toEntity(RosterDTO rosterDTO);

    default Roster fromId(Long id) {
        if (id == null) {
            return null;
        }
        Roster roster = new Roster();
        roster.setId(id);
        return roster;
    }
}
