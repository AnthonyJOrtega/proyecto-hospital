package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.EnfermedadAsserts.*;
import static com.mycompany.myapp.domain.EnfermedadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnfermedadMapperTest {

    private EnfermedadMapper enfermedadMapper;

    @BeforeEach
    void setUp() {
        enfermedadMapper = new EnfermedadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnfermedadSample1();
        var actual = enfermedadMapper.toEntity(enfermedadMapper.toDto(expected));
        assertEnfermedadAllPropertiesEquals(expected, actual);
    }
}
