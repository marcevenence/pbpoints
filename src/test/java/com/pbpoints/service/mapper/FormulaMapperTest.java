package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormulaMapperTest {

    private FormulaMapper formulaMapper;

    @BeforeEach
    public void setUp() {
        formulaMapper = new FormulaMapperImpl();
    }
}
