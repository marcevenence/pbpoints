package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamDetailPointMapperTest {

    private TeamDetailPointMapper teamDetailPointMapper;

    @BeforeEach
    public void setUp() {
        teamDetailPointMapper = new TeamDetailPointMapperImpl();
    }
}
