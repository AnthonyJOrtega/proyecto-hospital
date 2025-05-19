package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.InformeAsserts.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InformeMapperTest {

    private InformeMapper informeMapper;

    @BeforeEach
    void setUp() {
        informeMapper = new InformeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInformeSample1();
        var actual = informeMapper.toEntity(informeMapper.toDto(expected));
        assertInformeAllPropertiesEquals(expected, actual);
    }
}
