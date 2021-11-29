package com.pbpoints.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EquipmentMapperTest {

    private EquipmentMapper equipmentMapper;

    @BeforeEach
    public void setUp() {
        equipmentMapper = new EquipmentMapperImpl();
    }
}
