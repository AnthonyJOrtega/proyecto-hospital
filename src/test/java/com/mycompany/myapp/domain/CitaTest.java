package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CitaTestSamples.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = getCitaSample1();
        Cita cita2 = new Cita();
        assertThat(cita1).isNotEqualTo(cita2);

        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);

        cita2 = getCitaSample2();
        assertThat(cita1).isNotEqualTo(cita2);
    }

    @Test
    void informeTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Informe informeBack = getInformeRandomSampleGenerator();

        cita.setInforme(informeBack);
        assertThat(cita.getInforme()).isEqualTo(informeBack);

        cita.informe(null);
        assertThat(cita.getInforme()).isNull();
    }

    @Test
    void pacienteTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        cita.setPaciente(pacienteBack);
        assertThat(cita.getPaciente()).isEqualTo(pacienteBack);

        cita.paciente(null);
        assertThat(cita.getPaciente()).isNull();
    }

    @Test
    void trabajadorTest() {
        Cita cita = getCitaRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        cita.addTrabajador(trabajadorBack);
        assertThat(cita.getTrabajadors()).containsOnly(trabajadorBack);

        cita.removeTrabajador(trabajadorBack);
        assertThat(cita.getTrabajadors()).doesNotContain(trabajadorBack);

        cita.trabajadors(new HashSet<>(Set.of(trabajadorBack)));
        assertThat(cita.getTrabajadors()).containsOnly(trabajadorBack);

        cita.setTrabajadors(new HashSet<>());
        assertThat(cita.getTrabajadors()).doesNotContain(trabajadorBack);
    }
}
