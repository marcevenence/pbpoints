package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormatMapperTest {

    private FormatMapper formatMapper;

    @BeforeEach
    public void setUp() {
        formatMapper = new FormatMapperImpl();
    }
}
