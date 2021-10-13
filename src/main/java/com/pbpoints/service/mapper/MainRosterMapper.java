package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.MainRosterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MainRoster} and its DTO {@link MainRosterDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, UserExtraMapper.class })
public interface MainRosterMapper extends EntityMapper<MainRosterDTO, MainRoster> {
    @Mapping(target = "team", source = "team")
    @Mapping(target = "userExtra", source = "userExtra")
    MainRosterDTO toDto(MainRoster s);
}
