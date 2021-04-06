package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamDetailPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDetailPoint.class);
        TeamDetailPoint teamDetailPoint1 = new TeamDetailPoint();
        teamDetailPoint1.setId(1L);
        TeamDetailPoint teamDetailPoint2 = new TeamDetailPoint();
        teamDetailPoint2.setId(teamDetailPoint1.getId());
        assertThat(teamDetailPoint1).isEqualTo(teamDetailPoint2);
        teamDetailPoint2.setId(2L);
        assertThat(teamDetailPoint1).isNotEqualTo(teamDetailPoint2);
        teamDetailPoint1.setId(null);
        assertThat(teamDetailPoint1).isNotEqualTo(teamDetailPoint2);
    }
}
