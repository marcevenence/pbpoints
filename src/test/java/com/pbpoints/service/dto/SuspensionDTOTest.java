package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuspensionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuspensionDTO.class);
        SuspensionDTO suspensionDTO1 = new SuspensionDTO();
        suspensionDTO1.setId(1L);
        SuspensionDTO suspensionDTO2 = new SuspensionDTO();
        assertThat(suspensionDTO1).isNotEqualTo(suspensionDTO2);
        suspensionDTO2.setId(suspensionDTO1.getId());
        assertThat(suspensionDTO1).isEqualTo(suspensionDTO2);
        suspensionDTO2.setId(2L);
        assertThat(suspensionDTO1).isNotEqualTo(suspensionDTO2);
        suspensionDTO1.setId(null);
        assertThat(suspensionDTO1).isNotEqualTo(suspensionDTO2);
    }
}
