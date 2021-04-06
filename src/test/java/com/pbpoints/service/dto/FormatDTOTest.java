package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormatDTO.class);
        FormatDTO formatDTO1 = new FormatDTO();
        formatDTO1.setId(1L);
        FormatDTO formatDTO2 = new FormatDTO();
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO2.setId(formatDTO1.getId());
        assertThat(formatDTO1).isEqualTo(formatDTO2);
        formatDTO2.setId(2L);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO1.setId(null);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
    }
}
