package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CitaAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCitaAllPropertiesEquals(Cita expected, Cita actual) {
        assertCitaAutoGeneratedPropertiesEquals(expected, actual);
        assertCitaAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCitaAllUpdatablePropertiesEquals(Cita expected, Cita actual) {
        assertCitaUpdatableFieldsEquals(expected, actual);
        assertCitaUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCitaAutoGeneratedPropertiesEquals(Cita expected, Cita actual) {
        assertThat(actual)
            .as("Verify Cita auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCitaUpdatableFieldsEquals(Cita expected, Cita actual) {
        assertThat(actual)
            .as("Verify Cita relevant properties")
            .satisfies(a -> assertThat(a.getFechaCreacion()).as("check fechaCreacion").isEqualTo(expected.getFechaCreacion()))
            .satisfies(a -> assertThat(a.getEstadoCita()).as("check estadoCita").isEqualTo(expected.getEstadoCita()))
            .satisfies(a -> assertThat(a.getObservaciones()).as("check observaciones").isEqualTo(expected.getObservaciones()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCitaUpdatableRelationshipsEquals(Cita expected, Cita actual) {
        assertThat(actual)
            .as("Verify Cita relationships")
            .satisfies(a -> assertThat(a.getInforme()).as("check informe").isEqualTo(expected.getInforme()))
            .satisfies(a -> assertThat(a.getPaciente()).as("check paciente").isEqualTo(expected.getPaciente()))
            .satisfies(a -> assertThat(a.getTrabajadors()).as("check trabajadors").isEqualTo(expected.getTrabajadors()));
    }
}
