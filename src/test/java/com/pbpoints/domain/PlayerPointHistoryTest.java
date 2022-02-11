package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerPointHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPointHistory.class);
        PlayerPointHistory playerPointHistory1 = new PlayerPointHistory();
        playerPointHistory1.setId(1L);
        PlayerPointHistory playerPointHistory2 = new PlayerPointHistory();
        playerPointHistory2.setId(playerPointHistory1.getId());
        assertThat(playerPointHistory1).isEqualTo(playerPointHistory2);
        playerPointHistory2.setId(2L);
        assertThat(playerPointHistory1).isNotEqualTo(playerPointHistory2);
        playerPointHistory1.setId(null);
        assertThat(playerPointHistory1).isNotEqualTo(playerPointHistory2);
    }
}
