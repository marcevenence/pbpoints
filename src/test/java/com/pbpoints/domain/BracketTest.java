package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BracketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bracket.class);
        Bracket bracket1 = new Bracket();
        bracket1.setId(1L);
        Bracket bracket2 = new Bracket();
        bracket2.setId(bracket1.getId());
        assertThat(bracket1).isEqualTo(bracket2);
        bracket2.setId(2L);
        assertThat(bracket1).isNotEqualTo(bracket2);
        bracket1.setId(null);
        assertThat(bracket1).isNotEqualTo(bracket2);
    }
}
