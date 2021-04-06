package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RosterMapperTest {

    private RosterMapper rosterMapper;

    @BeforeEach
    public void setUp() {
        rosterMapper = new RosterMapperImpl();
    }
}
