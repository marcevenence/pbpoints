package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPoint.class);
        TeamPoint teamPoint1 = new TeamPoint();
        teamPoint1.setId(1L);
        TeamPoint teamPoint2 = new TeamPoint();
        teamPoint2.setId(teamPoint1.getId());
        assertThat(teamPoint1).isEqualTo(teamPoint2);
        teamPoint2.setId(2L);
        assertThat(teamPoint1).isNotEqualTo(teamPoint2);
        teamPoint1.setId(null);
        assertThat(teamPoint1).isNotEqualTo(teamPoint2);
    }
}
