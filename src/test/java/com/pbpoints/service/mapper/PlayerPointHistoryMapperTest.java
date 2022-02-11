package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerPointHistoryMapperTest {

    private PlayerPointHistoryMapper playerPointHistoryMapper;

    @BeforeEach
    public void setUp() {
        playerPointHistoryMapper = new PlayerPointHistoryMapperImpl();
    }
}
