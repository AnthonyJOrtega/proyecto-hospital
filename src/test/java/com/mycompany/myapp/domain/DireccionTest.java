package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DireccionTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direccion.class);
        Direccion direccion1 = getDireccionSample1();
        Direccion direccion2 = new Direccion();
        assertThat(direccion1).isNotEqualTo(direccion2);

        direccion2.setId(direccion1.getId());
        assertThat(direccion1).isEqualTo(direccion2);

        direccion2 = getDireccionSample2();
        assertThat(direccion1).isNotEqualTo(direccion2);
    }

    @Test
    void pacienteTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        direccion.addPaciente(pacienteBack);
        assertThat(direccion.getPacientes()).containsOnly(pacienteBack);

        direccion.removePaciente(pacienteBack);
        assertThat(direccion.getPacientes()).doesNotContain(pacienteBack);

        direccion.pacientes(new HashSet<>(Set.of(pacienteBack)));
        assertThat(direccion.getPacientes()).containsOnly(pacienteBack);

        direccion.setPacientes(new HashSet<>());
        assertThat(direccion.getPacientes()).doesNotContain(pacienteBack);
    }

    @Test
    void trabajadorTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        direccion.addTrabajador(trabajadorBack);
        assertThat(direccion.getTrabajadors()).containsOnly(trabajadorBack);

        direccion.removeTrabajador(trabajadorBack);
        assertThat(direccion.getTrabajadors()).doesNotContain(trabajadorBack);

        direccion.trabajadors(new HashSet<>(Set.of(trabajadorBack)));
        assertThat(direccion.getTrabajadors()).containsOnly(trabajadorBack);

        direccion.setTrabajadors(new HashSet<>());
        assertThat(direccion.getTrabajadors()).doesNotContain(trabajadorBack);
    }
}
