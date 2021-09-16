package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.SuspensionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Suspension} and its DTO {@link SuspensionDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SuspensionMapper extends EntityMapper<SuspensionDTO, Suspension> {
    @Mapping(target = "user", source = "user")
    SuspensionDTO toDto(Suspension s);
}
