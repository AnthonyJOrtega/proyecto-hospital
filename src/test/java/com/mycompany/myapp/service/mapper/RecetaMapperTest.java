package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.RecetaAsserts.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecetaMapperTest {

    private RecetaMapper recetaMapper;

    @BeforeEach
    void setUp() {
        recetaMapper = new RecetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRecetaSample1();
        var actual = recetaMapper.toEntity(recetaMapper.toDto(expected));
        assertRecetaAllPropertiesEquals(expected, actual);
    }
}
