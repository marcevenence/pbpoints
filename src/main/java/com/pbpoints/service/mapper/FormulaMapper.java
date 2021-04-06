package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.FormulaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formula} and its DTO {@link FormulaDTO}.
 */
@Mapper(componentModel = "spring", uses = { TournamentMapper.class })
public interface FormulaMapper extends EntityMapper<FormulaDTO, Formula> {
    @Mapping(target = "tournament", source = "tournament", qualifiedByName = "name")
    FormulaDTO toDto(Formula s);
}
