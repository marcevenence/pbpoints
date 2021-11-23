package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TournamentGroupDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TournamentGroupDTO.class);
        TournamentGroupDTO tournamentGroupDTO1 = new TournamentGroupDTO();
        tournamentGroupDTO1.setId(1L);
        TournamentGroupDTO tournamentGroupDTO2 = new TournamentGroupDTO();
        assertThat(tournamentGroupDTO1).isNotEqualTo(tournamentGroupDTO2);
        tournamentGroupDTO2.setId(tournamentGroupDTO1.getId());
        assertThat(tournamentGroupDTO1).isEqualTo(tournamentGroupDTO2);
        tournamentGroupDTO2.setId(2L);
        assertThat(tournamentGroupDTO1).isNotEqualTo(tournamentGroupDTO2);
        tournamentGroupDTO1.setId(null);
        assertThat(tournamentGroupDTO1).isNotEqualTo(tournamentGroupDTO2);
    }
}
