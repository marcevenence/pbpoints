package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerPointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPoint.class);
        PlayerPoint playerPoint1 = new PlayerPoint();
        playerPoint1.setId(1L);
        PlayerPoint playerPoint2 = new PlayerPoint();
        playerPoint2.setId(playerPoint1.getId());
        assertThat(playerPoint1).isEqualTo(playerPoint2);
        playerPoint2.setId(2L);
        assertThat(playerPoint1).isNotEqualTo(playerPoint2);
        playerPoint1.setId(null);
        assertThat(playerPoint1).isNotEqualTo(playerPoint2);
    }
}
