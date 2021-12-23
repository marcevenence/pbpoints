package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeamCategoryPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamCategoryPoint.class);
        TeamCategoryPoint teamCategoryPoint1 = new TeamCategoryPoint();
        teamCategoryPoint1.setId(1L);
        TeamCategoryPoint teamCategoryPoint2 = new TeamCategoryPoint();
        teamCategoryPoint2.setId(teamCategoryPoint1.getId());
        assertThat(teamCategoryPoint1).isEqualTo(teamCategoryPoint2);
        teamCategoryPoint2.setId(2L);
        assertThat(teamCategoryPoint1).isNotEqualTo(teamCategoryPoint2);
        teamCategoryPoint1.setId(null);
        assertThat(teamCategoryPoint1).isNotEqualTo(teamCategoryPoint2);
    }
}
