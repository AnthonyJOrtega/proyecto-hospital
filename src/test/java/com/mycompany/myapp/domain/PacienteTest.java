package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CitaTestSamples.*;
import static com.mycompany.myapp.domain.DireccionTestSamples.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void trabajadorTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        paciente.addTrabajador(trabajadorBack);
        assertThat(paciente.getTrabajadors()).containsOnly(trabajadorBack);

        paciente.removeTrabajador(trabajadorBack);
        assertThat(paciente.getTrabajadors()).doesNotContain(trabajadorBack);

        paciente.trabajadors(new HashSet<>(Set.of(trabajadorBack)));
        assertThat(paciente.getTrabajadors()).containsOnly(trabajadorBack);

        paciente.setTrabajadors(new HashSet<>());
        assertThat(paciente.getTrabajadors()).doesNotContain(trabajadorBack);
    }

    @Test
    void citaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        paciente.addCita(citaBack);
        assertThat(paciente.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getPaciente()).isEqualTo(paciente);

        paciente.removeCita(citaBack);
        assertThat(paciente.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getPaciente()).isNull();

        paciente.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(paciente.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getPaciente()).isEqualTo(paciente);

        paciente.setCitas(new HashSet<>());
        assertThat(paciente.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getPaciente()).isNull();
    }

    @Test
    void informeTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Informe informeBack = getInformeRandomSampleGenerator();

        paciente.addInforme(informeBack);
        assertThat(paciente.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getPaciente()).isEqualTo(paciente);

        paciente.removeInforme(informeBack);
        assertThat(paciente.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getPaciente()).isNull();

        paciente.informes(new HashSet<>(Set.of(informeBack)));
        assertThat(paciente.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getPaciente()).isEqualTo(paciente);

        paciente.setInformes(new HashSet<>());
        assertThat(paciente.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getPaciente()).isNull();
    }

    @Test
    void recetaTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Receta recetaBack = getRecetaRandomSampleGenerator();

        paciente.addReceta(recetaBack);
        assertThat(paciente.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getPaciente()).isEqualTo(paciente);

        paciente.removeReceta(recetaBack);
        assertThat(paciente.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getPaciente()).isNull();

        paciente.recetas(new HashSet<>(Set.of(recetaBack)));
        assertThat(paciente.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getPaciente()).isEqualTo(paciente);

        paciente.setRecetas(new HashSet<>());
        assertThat(paciente.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getPaciente()).isNull();
    }

    @Test
    void direccionTest() {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        paciente.addDireccion(direccionBack);
        assertThat(paciente.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getPacientes()).containsOnly(paciente);

        paciente.removeDireccion(direccionBack);
        assertThat(paciente.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getPacientes()).doesNotContain(paciente);

        paciente.direccions(new HashSet<>(Set.of(direccionBack)));
        assertThat(paciente.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getPacientes()).containsOnly(paciente);

        paciente.setDireccions(new HashSet<>());
        assertThat(paciente.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getPacientes()).doesNotContain(paciente);
    }
}
