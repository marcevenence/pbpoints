package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.MainRosterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MainRoster} and its DTO {@link MainRosterDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, UserMapper.class })
public interface MainRosterMapper extends EntityMapper<MainRosterDTO, MainRoster> {
    @Mapping(target = "team", source = "team")
    @Mapping(target = "user", source = "user")
    MainRosterDTO toDto(MainRoster s);
}
