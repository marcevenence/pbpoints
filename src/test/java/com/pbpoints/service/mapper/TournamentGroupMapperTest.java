package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TournamentGroupMapperTest {

    private TournamentGroupMapper tournamentGroupMapper;

    @BeforeEach
    public void setUp() {
        tournamentGroupMapper = new TournamentGroupMapperImpl();
    }
}
