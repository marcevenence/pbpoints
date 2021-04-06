package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPointDTO.class);
        PlayerPointDTO playerPointDTO1 = new PlayerPointDTO();
        playerPointDTO1.setId(1L);
        PlayerPointDTO playerPointDTO2 = new PlayerPointDTO();
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
        playerPointDTO2.setId(playerPointDTO1.getId());
        assertThat(playerPointDTO1).isEqualTo(playerPointDTO2);
        playerPointDTO2.setId(2L);
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
        playerPointDTO1.setId(null);
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
    }
}
