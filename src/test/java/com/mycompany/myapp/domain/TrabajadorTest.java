package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CitaTestSamples.*;
import static com.mycompany.myapp.domain.DireccionTestSamples.*;
import static com.mycompany.myapp.domain.EspecialidadTestSamples.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrabajadorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trabajador.class);
        Trabajador trabajador1 = getTrabajadorSample1();
        Trabajador trabajador2 = new Trabajador();
        assertThat(trabajador1).isNotEqualTo(trabajador2);

        trabajador2.setId(trabajador1.getId());
        assertThat(trabajador1).isEqualTo(trabajador2);

        trabajador2 = getTrabajadorSample2();
        assertThat(trabajador1).isNotEqualTo(trabajador2);
    }

    @Test
    void especialidadTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Especialidad especialidadBack = getEspecialidadRandomSampleGenerator();

        trabajador.addEspecialidad(especialidadBack);
        assertThat(trabajador.getEspecialidads()).containsOnly(especialidadBack);

        trabajador.removeEspecialidad(especialidadBack);
        assertThat(trabajador.getEspecialidads()).doesNotContain(especialidadBack);

        trabajador.especialidads(new HashSet<>(Set.of(especialidadBack)));
        assertThat(trabajador.getEspecialidads()).containsOnly(especialidadBack);

        trabajador.setEspecialidads(new HashSet<>());
        assertThat(trabajador.getEspecialidads()).doesNotContain(especialidadBack);
    }

    @Test
    void informeTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Informe informeBack = getInformeRandomSampleGenerator();

        trabajador.addInforme(informeBack);
        assertThat(trabajador.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getTrabajador()).isEqualTo(trabajador);

        trabajador.removeInforme(informeBack);
        assertThat(trabajador.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getTrabajador()).isNull();

        trabajador.informes(new HashSet<>(Set.of(informeBack)));
        assertThat(trabajador.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getTrabajador()).isEqualTo(trabajador);

        trabajador.setInformes(new HashSet<>());
        assertThat(trabajador.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getTrabajador()).isNull();
    }

    @Test
    void recetaTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Receta recetaBack = getRecetaRandomSampleGenerator();

        trabajador.addReceta(recetaBack);
        assertThat(trabajador.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getTrabajador()).isEqualTo(trabajador);

        trabajador.removeReceta(recetaBack);
        assertThat(trabajador.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getTrabajador()).isNull();

        trabajador.recetas(new HashSet<>(Set.of(recetaBack)));
        assertThat(trabajador.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getTrabajador()).isEqualTo(trabajador);

        trabajador.setRecetas(new HashSet<>());
        assertThat(trabajador.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getTrabajador()).isNull();
    }

    @Test
    void citaTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        trabajador.addCita(citaBack);
        assertThat(trabajador.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.removeCita(citaBack);
        assertThat(trabajador.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getTrabajadors()).doesNotContain(trabajador);

        trabajador.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(trabajador.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.setCitas(new HashSet<>());
        assertThat(trabajador.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getTrabajadors()).doesNotContain(trabajador);
    }

    @Test
    void pacienteTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        trabajador.addPaciente(pacienteBack);
        assertThat(trabajador.getPacientes()).containsOnly(pacienteBack);
        assertThat(pacienteBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.removePaciente(pacienteBack);
        assertThat(trabajador.getPacientes()).doesNotContain(pacienteBack);
        assertThat(pacienteBack.getTrabajadors()).doesNotContain(trabajador);

        trabajador.pacientes(new HashSet<>(Set.of(pacienteBack)));
        assertThat(trabajador.getPacientes()).containsOnly(pacienteBack);
        assertThat(pacienteBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.setPacientes(new HashSet<>());
        assertThat(trabajador.getPacientes()).doesNotContain(pacienteBack);
        assertThat(pacienteBack.getTrabajadors()).doesNotContain(trabajador);
    }

    @Test
    void direccionTest() {
        Trabajador trabajador = getTrabajadorRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        trabajador.addDireccion(direccionBack);
        assertThat(trabajador.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.removeDireccion(direccionBack);
        assertThat(trabajador.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getTrabajadors()).doesNotContain(trabajador);

        trabajador.direccions(new HashSet<>(Set.of(direccionBack)));
        assertThat(trabajador.getDireccions()).containsOnly(direccionBack);
        assertThat(direccionBack.getTrabajadors()).containsOnly(trabajador);

        trabajador.setDireccions(new HashSet<>());
        assertThat(trabajador.getDireccions()).doesNotContain(direccionBack);
        assertThat(direccionBack.getTrabajadors()).doesNotContain(trabajador);
    }
}
