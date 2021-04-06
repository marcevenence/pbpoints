package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamDetailPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDetailPointDTO.class);
        TeamDetailPointDTO teamDetailPointDTO1 = new TeamDetailPointDTO();
        teamDetailPointDTO1.setId(1L);
        TeamDetailPointDTO teamDetailPointDTO2 = new TeamDetailPointDTO();
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO2.setId(teamDetailPointDTO1.getId());
        assertThat(teamDetailPointDTO1).isEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO2.setId(2L);
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO1.setId(null);
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
    }
}
