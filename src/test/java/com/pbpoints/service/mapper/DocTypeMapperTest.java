package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocTypeMapperTest {

    private DocTypeMapper docTypeMapper;

    @BeforeEach
    public void setUp() {
        docTypeMapper = new DocTypeMapperImpl();
    }
}
