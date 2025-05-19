package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EspecialidadTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EspecialidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialidad.class);
        Especialidad especialidad1 = getEspecialidadSample1();
        Especialidad especialidad2 = new Especialidad();
        assertThat(especialidad1).isNotEqualTo(especialidad2);

        especialidad2.setId(especialidad1.getId());
        assertThat(especialidad1).isEqualTo(especialidad2);

        especialidad2 = getEspecialidadSample2();
        assertThat(especialidad1).isNotEqualTo(especialidad2);
    }

    @Test
    void trabajadorTest() {
        Especialidad especialidad = getEspecialidadRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        especialidad.addTrabajador(trabajadorBack);
        assertThat(especialidad.getTrabajadors()).containsOnly(trabajadorBack);
        assertThat(trabajadorBack.getEspecialidads()).containsOnly(especialidad);

        especialidad.removeTrabajador(trabajadorBack);
        assertThat(especialidad.getTrabajadors()).doesNotContain(trabajadorBack);
        assertThat(trabajadorBack.getEspecialidads()).doesNotContain(especialidad);

        especialidad.trabajadors(new HashSet<>(Set.of(trabajadorBack)));
        assertThat(especialidad.getTrabajadors()).containsOnly(trabajadorBack);
        assertThat(trabajadorBack.getEspecialidads()).containsOnly(especialidad);

        especialidad.setTrabajadors(new HashSet<>());
        assertThat(especialidad.getTrabajadors()).doesNotContain(trabajadorBack);
        assertThat(trabajadorBack.getEspecialidads()).doesNotContain(especialidad);
    }
}
