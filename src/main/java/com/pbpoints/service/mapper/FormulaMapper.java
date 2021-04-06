package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.FormulaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Formula} and its DTO {@link FormulaDTO}.
 */
@Mapper(componentModel = "spring", uses = { com.pbpoints.service.mapper.TournamentMapper.class })
public interface FormulaMapper extends com.pbpoints.service.mapper.EntityMapper<FormulaDTO, Formula> {
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    FormulaDTO toDto(Formula formula);

    @Mapping(source = "tournamentId", target = "tournament")
    Formula toEntity(FormulaDTO formulaDTO);

    default Formula fromId(Long id) {
        if (id == null) {
            return null;
        }
        Formula formula = new Formula();
        formula.setId(id);
        return formula;
    }
}
