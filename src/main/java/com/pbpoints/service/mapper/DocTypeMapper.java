package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.DocTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocType} and its DTO {@link DocTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocTypeMapper extends EntityMapper<DocTypeDTO, DocType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocTypeDTO toDtoId(DocType docType);
}
