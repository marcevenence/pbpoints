package com.pbpoints.service.mapper;

import com.pbpoints.domain.*;
import com.pbpoints.service.dto.DocTypeDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link DocType} and its DTO {@link DocTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocTypeMapper extends com.pbpoints.service.mapper.EntityMapper<DocTypeDTO, DocType> {
    default DocType fromId(Long id) {
        if (id == null) {
            return null;
        }
        DocType docType = new DocType();
        docType.setId(id);
        return docType;
    }
}
