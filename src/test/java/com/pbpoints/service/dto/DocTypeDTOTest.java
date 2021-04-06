package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocTypeDTO.class);
        DocTypeDTO docTypeDTO1 = new DocTypeDTO();
        docTypeDTO1.setId(1L);
        DocTypeDTO docTypeDTO2 = new DocTypeDTO();
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
        docTypeDTO2.setId(docTypeDTO1.getId());
        assertThat(docTypeDTO1).isEqualTo(docTypeDTO2);
        docTypeDTO2.setId(2L);
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
        docTypeDTO1.setId(null);
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
    }
}
