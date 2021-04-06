package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerDetailPointMapperTest {

    private PlayerDetailPointMapper playerDetailPointMapper;

    @BeforeEach
    public void setUp() {
        playerDetailPointMapper = new PlayerDetailPointMapperImpl();
    }
}
