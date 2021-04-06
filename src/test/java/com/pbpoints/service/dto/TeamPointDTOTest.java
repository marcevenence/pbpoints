package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPointDTO.class);
        TeamPointDTO teamPointDTO1 = new TeamPointDTO();
        teamPointDTO1.setId(1L);
        TeamPointDTO teamPointDTO2 = new TeamPointDTO();
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
        teamPointDTO2.setId(teamPointDTO1.getId());
        assertThat(teamPointDTO1).isEqualTo(teamPointDTO2);
        teamPointDTO2.setId(2L);
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
        teamPointDTO1.setId(null);
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
    }
}
