package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.TrabajadorAsserts.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrabajadorMapperTest {

    private TrabajadorMapper trabajadorMapper;

    @BeforeEach
    void setUp() {
        trabajadorMapper = new TrabajadorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrabajadorSample1();
        var actual = trabajadorMapper.toEntity(trabajadorMapper.toDto(expected));
        assertTrabajadorAllPropertiesEquals(expected, actual);
    }
}
