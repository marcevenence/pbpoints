package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerPointHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPointHistoryDTO.class);
        PlayerPointHistoryDTO playerPointHistoryDTO1 = new PlayerPointHistoryDTO();
        playerPointHistoryDTO1.setId(1L);
        PlayerPointHistoryDTO playerPointHistoryDTO2 = new PlayerPointHistoryDTO();
        assertThat(playerPointHistoryDTO1).isNotEqualTo(playerPointHistoryDTO2);
        playerPointHistoryDTO2.setId(playerPointHistoryDTO1.getId());
        assertThat(playerPointHistoryDTO1).isEqualTo(playerPointHistoryDTO2);
        playerPointHistoryDTO2.setId(2L);
        assertThat(playerPointHistoryDTO1).isNotEqualTo(playerPointHistoryDTO2);
        playerPointHistoryDTO1.setId(null);
        assertThat(playerPointHistoryDTO1).isNotEqualTo(playerPointHistoryDTO2);
    }
}
