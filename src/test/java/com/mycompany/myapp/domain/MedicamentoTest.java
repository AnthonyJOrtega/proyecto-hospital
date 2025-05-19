package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MedicamentoTestSamples.*;
import static com.mycompany.myapp.domain.RecetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MedicamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicamento.class);
        Medicamento medicamento1 = getMedicamentoSample1();
        Medicamento medicamento2 = new Medicamento();
        assertThat(medicamento1).isNotEqualTo(medicamento2);

        medicamento2.setId(medicamento1.getId());
        assertThat(medicamento1).isEqualTo(medicamento2);

        medicamento2 = getMedicamentoSample2();
        assertThat(medicamento1).isNotEqualTo(medicamento2);
    }

    @Test
    void recetaTest() {
        Medicamento medicamento = getMedicamentoRandomSampleGenerator();
        Receta recetaBack = getRecetaRandomSampleGenerator();

        medicamento.addReceta(recetaBack);
        assertThat(medicamento.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getMedicamentos()).containsOnly(medicamento);

        medicamento.removeReceta(recetaBack);
        assertThat(medicamento.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getMedicamentos()).doesNotContain(medicamento);

        medicamento.recetas(new HashSet<>(Set.of(recetaBack)));
        assertThat(medicamento.getRecetas()).containsOnly(recetaBack);
        assertThat(recetaBack.getMedicamentos()).containsOnly(medicamento);

        medicamento.setRecetas(new HashSet<>());
        assertThat(medicamento.getRecetas()).doesNotContain(recetaBack);
        assertThat(recetaBack.getMedicamentos()).doesNotContain(medicamento);
    }
}
