package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamCategoryPointMapperTest {

    private TeamCategoryPointMapper teamCategoryPointMapper;

    @BeforeEach
    public void setUp() {
        teamCategoryPointMapper = new TeamCategoryPointMapperImpl();
    }
}
