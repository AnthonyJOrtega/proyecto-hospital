package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnfermedadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnfermedadDTO.class);
        EnfermedadDTO enfermedadDTO1 = new EnfermedadDTO();
        enfermedadDTO1.setId(1L);
        EnfermedadDTO enfermedadDTO2 = new EnfermedadDTO();
        assertThat(enfermedadDTO1).isNotEqualTo(enfermedadDTO2);
        enfermedadDTO2.setId(enfermedadDTO1.getId());
        assertThat(enfermedadDTO1).isEqualTo(enfermedadDTO2);
        enfermedadDTO2.setId(2L);
        assertThat(enfermedadDTO1).isNotEqualTo(enfermedadDTO2);
        enfermedadDTO1.setId(null);
        assertThat(enfermedadDTO1).isNotEqualTo(enfermedadDTO2);
    }
}
