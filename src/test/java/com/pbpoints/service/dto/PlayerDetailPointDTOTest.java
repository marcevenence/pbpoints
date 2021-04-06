package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerDetailPointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDetailPointDTO.class);
        PlayerDetailPointDTO playerDetailPointDTO1 = new PlayerDetailPointDTO();
        playerDetailPointDTO1.setId(1L);
        PlayerDetailPointDTO playerDetailPointDTO2 = new PlayerDetailPointDTO();
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO2.setId(playerDetailPointDTO1.getId());
        assertThat(playerDetailPointDTO1).isEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO2.setId(2L);
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO1.setId(null);
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
    }
}
