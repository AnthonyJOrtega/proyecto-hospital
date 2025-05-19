package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static com.mycompany.myapp.domain.MedicamentoTestSamples.*;
import static com.mycompany.myapp.domain.PacienteTestSamples.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;
import static com.mycompany.myapp.domain.TrabajadorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Receta.class);
        Receta receta1 = getRecetaSample1();
        Receta receta2 = new Receta();
        assertThat(receta1).isNotEqualTo(receta2);

        receta2.setId(receta1.getId());
        assertThat(receta1).isEqualTo(receta2);

        receta2 = getRecetaSample2();
        assertThat(receta1).isNotEqualTo(receta2);
    }

    @Test
    void pacienteTest() {
        Receta receta = getRecetaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        receta.setPaciente(pacienteBack);
        assertThat(receta.getPaciente()).isEqualTo(pacienteBack);

        receta.paciente(null);
        assertThat(receta.getPaciente()).isNull();
    }

    @Test
    void trabajadorTest() {
        Receta receta = getRecetaRandomSampleGenerator();
        Trabajador trabajadorBack = getTrabajadorRandomSampleGenerator();

        receta.setTrabajador(trabajadorBack);
        assertThat(receta.getTrabajador()).isEqualTo(trabajadorBack);

        receta.trabajador(null);
        assertThat(receta.getTrabajador()).isNull();
    }

    @Test
    void medicamentoTest() {
        Receta receta = getRecetaRandomSampleGenerator();
        Medicamento medicamentoBack = getMedicamentoRandomSampleGenerator();

        receta.addMedicamento(medicamentoBack);
        assertThat(receta.getMedicamentos()).containsOnly(medicamentoBack);

        receta.removeMedicamento(medicamentoBack);
        assertThat(receta.getMedicamentos()).doesNotContain(medicamentoBack);

        receta.medicamentos(new HashSet<>(Set.of(medicamentoBack)));
        assertThat(receta.getMedicamentos()).containsOnly(medicamentoBack);

        receta.setMedicamentos(new HashSet<>());
        assertThat(receta.getMedicamentos()).doesNotContain(medicamentoBack);
    }

    @Test
    void informeTest() {
        Receta receta = getRecetaRandomSampleGenerator();
        Informe informeBack = getInformeRandomSampleGenerator();

        receta.setInforme(informeBack);
        assertThat(receta.getInforme()).isEqualTo(informeBack);
        assertThat(informeBack.getReceta()).isEqualTo(receta);

        receta.informe(null);
        assertThat(receta.getInforme()).isNull();
        assertThat(informeBack.getReceta()).isNull();
    }
}
