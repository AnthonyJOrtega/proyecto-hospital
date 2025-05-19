package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.EspecialidadAsserts.*;
import static com.mycompany.myapp.domain.EspecialidadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EspecialidadMapperTest {

    private EspecialidadMapper especialidadMapper;

    @BeforeEach
    void setUp() {
        especialidadMapper = new EspecialidadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEspecialidadSample1();
        var actual = especialidadMapper.toEntity(especialidadMapper.toDto(expected));
        assertEspecialidadAllPropertiesEquals(expected, actual);
    }
}
