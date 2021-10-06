package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MainRosterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainRosterDTO.class);
        MainRosterDTO mainRosterDTO1 = new MainRosterDTO();
        mainRosterDTO1.setId(1L);
        MainRosterDTO mainRosterDTO2 = new MainRosterDTO();
        assertThat(mainRosterDTO1).isNotEqualTo(mainRosterDTO2);
        mainRosterDTO2.setId(mainRosterDTO1.getId());
        assertThat(mainRosterDTO1).isEqualTo(mainRosterDTO2);
        mainRosterDTO2.setId(2L);
        assertThat(mainRosterDTO1).isNotEqualTo(mainRosterDTO2);
        mainRosterDTO1.setId(null);
        assertThat(mainRosterDTO1).isNotEqualTo(mainRosterDTO2);
    }
}
