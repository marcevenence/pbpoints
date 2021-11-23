package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TournamentGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TournamentGroup.class);
        TournamentGroup tournamentGroup1 = new TournamentGroup();
        tournamentGroup1.setId(1L);
        TournamentGroup tournamentGroup2 = new TournamentGroup();
        tournamentGroup2.setId(tournamentGroup1.getId());
        assertThat(tournamentGroup1).isEqualTo(tournamentGroup2);
        tournamentGroup2.setId(2L);
        assertThat(tournamentGroup1).isNotEqualTo(tournamentGroup2);
        tournamentGroup1.setId(null);
        assertThat(tournamentGroup1).isNotEqualTo(tournamentGroup2);
    }
}
