package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InformeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InformeDTO.class);
        InformeDTO informeDTO1 = new InformeDTO();
        informeDTO1.setId(1L);
        InformeDTO informeDTO2 = new InformeDTO();
        assertThat(informeDTO1).isNotEqualTo(informeDTO2);
        informeDTO2.setId(informeDTO1.getId());
        assertThat(informeDTO1).isEqualTo(informeDTO2);
        informeDTO2.setId(2L);
        assertThat(informeDTO1).isNotEqualTo(informeDTO2);
        informeDTO1.setId(null);
        assertThat(informeDTO1).isNotEqualTo(informeDTO2);
    }
}
