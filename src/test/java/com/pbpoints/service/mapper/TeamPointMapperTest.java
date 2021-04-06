package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamPointMapperTest {

    private TeamPointMapper teamPointMapper;

    @BeforeEach
    public void setUp() {
        teamPointMapper = new TeamPointMapperImpl();
    }
}
