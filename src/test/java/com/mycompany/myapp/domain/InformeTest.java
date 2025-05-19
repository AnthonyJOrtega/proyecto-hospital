package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CitaTestSamples.*;
import static com.mycompany.myapp.domain.EnfermedadTestSamples.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InformeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Informe.class);
        Informe informe1 = getInformeSample1();
        Informe informe2 = new Informe();
        assertThat(informe1).isNotEqualTo(informe2);

        informe2.setId(informe1.getId());
        assertThat(informe1).isEqualTo(informe2);

        informe2 = getInformeSample2();
        assertThat(informe1).isNotEqualTo(informe2);
    }

    @Test
    void recetaTest() {
        Informe informe = getInformeRandomSampleGenerator();
        Receta recetaBack = getRecetaRandomSampleGenerator();

        informe.setReceta(recetaBack);
        assertThat(informe.getReceta()).isEqualTo(recetaBack);

        informe.receta(null);
        assertThat(informe.getReceta()).isNull();
    }

    @Test
    void pacienteTest() {
        Informe informe = getInformeRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        informe.setPaciente(pacienteBack);
        assertThat(informe.getPaciente()).isEqualTo(pacienteBack);

        informe.paciente(null);
        assertThat(informe.getPaciente()).isNull();
    }

    @Test
    void trabajadorTest() {
        Informe informe = getInformeRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        informe.setTrabajador(trabajadorBack);
        assertThat(informe.getTrabajador()).isEqualTo(trabajadorBack);

        informe.trabajador(null);
        assertThat(informe.getTrabajador()).isNull();
    }

    @Test
    void enfermedadTest() {
        Informe informe = getInformeRandomSampleGenerator();
        Enfermedad enfermedadBack = getEnfermedadRandomSampleGenerator();

        informe.addEnfermedad(enfermedadBack);
        assertThat(informe.getEnfermedads()).containsOnly(enfermedadBack);

        informe.removeEnfermedad(enfermedadBack);
        assertThat(informe.getEnfermedads()).doesNotContain(enfermedadBack);

        informe.enfermedads(new HashSet<>(Set.of(enfermedadBack)));
        assertThat(informe.getEnfermedads()).containsOnly(enfermedadBack);

        informe.setEnfermedads(new HashSet<>());
        assertThat(informe.getEnfermedads()).doesNotContain(enfermedadBack);
    }

    @Test
    void citaTest() {
        Informe informe = getInformeRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        informe.setCita(citaBack);
        assertThat(informe.getCita()).isEqualTo(citaBack);
        assertThat(citaBack.getInforme()).isEqualTo(informe);

        informe.cita(null);
        assertThat(informe.getCita()).isNull();
        assertThat(citaBack.getInforme()).isNull();
    }
}
