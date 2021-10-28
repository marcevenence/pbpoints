package com.pbpoints.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SponsorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SponsorDTO.class);
        SponsorDTO sponsorDTO1 = new SponsorDTO();
        sponsorDTO1.setId(1L);
        SponsorDTO sponsorDTO2 = new SponsorDTO();
        assertThat(sponsorDTO1).isNotEqualTo(sponsorDTO2);
        sponsorDTO2.setId(sponsorDTO1.getId());
        assertThat(sponsorDTO1).isEqualTo(sponsorDTO2);
        sponsorDTO2.setId(2L);
        assertThat(sponsorDTO1).isNotEqualTo(sponsorDTO2);
        sponsorDTO1.setId(null);
        assertThat(sponsorDTO1).isNotEqualTo(sponsorDTO2);
    }
}
