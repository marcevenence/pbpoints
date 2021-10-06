package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainRosterMapperTest {

    private MainRosterMapper mainRosterMapper;

    @BeforeEach
    public void setUp() {
        mainRosterMapper = new MainRosterMapperImpl();
    }
}
