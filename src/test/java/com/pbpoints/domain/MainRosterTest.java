package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MainRosterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainRoster.class);
        MainRoster mainRoster1 = new MainRoster();
        mainRoster1.setId(1L);
        MainRoster mainRoster2 = new MainRoster();
        mainRoster2.setId(mainRoster1.getId());
        assertThat(mainRoster1).isEqualTo(mainRoster2);
        mainRoster2.setId(2L);
        assertThat(mainRoster1).isNotEqualTo(mainRoster2);
        mainRoster1.setId(null);
        assertThat(mainRoster1).isNotEqualTo(mainRoster2);
    }
}
