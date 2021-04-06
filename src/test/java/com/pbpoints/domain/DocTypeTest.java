package com.pbpoints.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.pbpoints.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocType.class);
        DocType docType1 = new DocType();
        docType1.setId(1L);
        DocType docType2 = new DocType();
        docType2.setId(docType1.getId());
        assertThat(docType1).isEqualTo(docType2);
        docType2.setId(2L);
        assertThat(docType1).isNotEqualTo(docType2);
        docType1.setId(null);
        assertThat(docType1).isNotEqualTo(docType2);
    }
}
