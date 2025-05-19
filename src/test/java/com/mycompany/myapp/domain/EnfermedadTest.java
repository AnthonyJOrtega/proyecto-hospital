package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EnfermedadTestSamples.*;
import static com.mycompany.myapp.domain.InformeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EnfermedadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enfermedad.class);
        Enfermedad enfermedad1 = getEnfermedadSample1();
        Enfermedad enfermedad2 = new Enfermedad();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);

        enfermedad2.setId(enfermedad1.getId());
        assertThat(enfermedad1).isEqualTo(enfermedad2);

        enfermedad2 = getEnfermedadSample2();
        assertThat(enfermedad1).isNotEqualTo(enfermedad2);
    }

    @Test
    void informeTest() {
        Enfermedad enfermedad = getEnfermedadRandomSampleGenerator();
        Informe informeBack = getInformeRandomSampleGenerator();

        enfermedad.addInforme(informeBack);
        assertThat(enfermedad.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getEnfermedads()).containsOnly(enfermedad);

        enfermedad.removeInforme(informeBack);
        assertThat(enfermedad.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getEnfermedads()).doesNotContain(enfermedad);

        enfermedad.informes(new HashSet<>(Set.of(informeBack)));
        assertThat(enfermedad.getInformes()).containsOnly(informeBack);
        assertThat(informeBack.getEnfermedads()).containsOnly(enfermedad);

        enfermedad.setInformes(new HashSet<>());
        assertThat(enfermedad.getInformes()).doesNotContain(informeBack);
        assertThat(informeBack.getEnfermedads()).doesNotContain(enfermedad);
    }
}
