package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SuspensionMapperTest {

    private SuspensionMapper suspensionMapper;

    @BeforeEach
    public void setUp() {
        suspensionMapper = new SuspensionMapperImpl();
    }
}
