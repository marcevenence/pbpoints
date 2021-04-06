package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerDetailPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDetailPoint.class);
        PlayerDetailPoint playerDetailPoint1 = new PlayerDetailPoint();
        playerDetailPoint1.setId(1L);
        PlayerDetailPoint playerDetailPoint2 = new PlayerDetailPoint();
        playerDetailPoint2.setId(playerDetailPoint1.getId());
        assertThat(playerDetailPoint1).isEqualTo(playerDetailPoint2);
        playerDetailPoint2.setId(2L);
        assertThat(playerDetailPoint1).isNotEqualTo(playerDetailPoint2);
        playerDetailPoint1.setId(null);
        assertThat(playerDetailPoint1).isNotEqualTo(playerDetailPoint2);
    }
}
