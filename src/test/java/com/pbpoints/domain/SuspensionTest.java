package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuspensionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suspension.class);
        Suspension suspension1 = new Suspension();
        suspension1.setId(1L);
        Suspension suspension2 = new Suspension();
        suspension2.setId(suspension1.getId());
        assertThat(suspension1).isEqualTo(suspension2);
        suspension2.setId(2L);
        assertThat(suspension1).isNotEqualTo(suspension2);
        suspension1.setId(null);
        assertThat(suspension1).isNotEqualTo(suspension2);
    }
}
