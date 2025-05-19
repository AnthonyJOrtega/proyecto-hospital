package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrabajadorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrabajadorDTO.class);
        TrabajadorDTO trabajadorDTO1 = new TrabajadorDTO();
        trabajadorDTO1.setId(1L);
        TrabajadorDTO trabajadorDTO2 = new TrabajadorDTO();
        assertThat(trabajadorDTO1).isNotEqualTo(trabajadorDTO2);
        trabajadorDTO2.setId(trabajadorDTO1.getId());
        assertThat(trabajadorDTO1).isEqualTo(trabajadorDTO2);
        trabajadorDTO2.setId(2L);
        assertThat(trabajadorDTO1).isNotEqualTo(trabajadorDTO2);
        trabajadorDTO1.setId(null);
        assertThat(trabajadorDTO1).isNotEqualTo(trabajadorDTO2);
    }
}
