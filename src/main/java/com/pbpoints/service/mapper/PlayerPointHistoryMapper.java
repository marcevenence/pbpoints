package com.pbpoints.service.mapper;

import com.pbpoints.domain.PlayerPointHistory;
import com.pbpoints.service.dto.PlayerPointHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerPointHistory} and its DTO {@link PlayerPointHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlayerPointMapper.class, CategoryMapper.class, SeasonMapper.class })
public interface PlayerPointHistoryMapper extends EntityMapper<PlayerPointHistoryDTO, PlayerPointHistory> {
    @Mapping(target = "playerPoint", source = "playerPoint")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "season", source = "season")
    PlayerPointHistoryDTO toDto(PlayerPointHistory s);
}
