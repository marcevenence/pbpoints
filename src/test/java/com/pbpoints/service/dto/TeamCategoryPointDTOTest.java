package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamCategoryPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamCategoryPointDTO.class);
        TeamCategoryPointDTO teamCategoryPointDTO1 = new TeamCategoryPointDTO();
        teamCategoryPointDTO1.setId(1L);
        TeamCategoryPointDTO teamCategoryPointDTO2 = new TeamCategoryPointDTO();
        assertThat(teamCategoryPointDTO1).isNotEqualTo(teamCategoryPointDTO2);
        teamCategoryPointDTO2.setId(teamCategoryPointDTO1.getId());
        assertThat(teamCategoryPointDTO1).isEqualTo(teamCategoryPointDTO2);
        teamCategoryPointDTO2.setId(2L);
        assertThat(teamCategoryPointDTO1).isNotEqualTo(teamCategoryPointDTO2);
        teamCategoryPointDTO1.setId(null);
        assertThat(teamCategoryPointDTO1).isNotEqualTo(teamCategoryPointDTO2);
    }
}
