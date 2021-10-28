package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SponsorMapperTest {

    private SponsorMapper sponsorMapper;

    @BeforeEach
    public void setUp() {
        sponsorMapper = new SponsorMapperImpl();
    }
}
