package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrabajadorCriteriaTest {

    @Test
    void newTrabajadorCriteriaHasAllFiltersNullTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        assertThat(trabajadorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trabajadorCriteriaFluentMethodsCreatesFiltersTest() {
        var trabajadorCriteria = new TrabajadorCriteria();

        setAllFilters(trabajadorCriteria);

        assertThat(trabajadorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trabajadorCriteriaCopyCreatesNullFilterTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        var copy = trabajadorCriteria.copy();

        assertThat(trabajadorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trabajadorCriteria)
        );
    }

    @Test
    void trabajadorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trabajadorCriteria = new TrabajadorCriteria();
        setAllFilters(trabajadorCriteria);

        var copy = trabajadorCriteria.copy();

        assertThat(trabajadorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trabajadorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trabajadorCriteria = new TrabajadorCriteria();

        assertThat(trabajadorCriteria).hasToString("TrabajadorCriteria{}");
    }

    private static void setAllFilters(TrabajadorCriteria trabajadorCriteria) {
        trabajadorCriteria.id();
        trabajadorCriteria.idUsuario();
        trabajadorCriteria.nombre();
        trabajadorCriteria.apellido();
        trabajadorCriteria.dni();
        trabajadorCriteria.puesto();
        trabajadorCriteria.disponibilidad();
        trabajadorCriteria.turno();
        trabajadorCriteria.especialidadId();
        trabajadorCriteria.informeId();
        trabajadorCriteria.recetaId();
        trabajadorCriteria.citaId();
        trabajadorCriteria.pacienteId();
        trabajadorCriteria.direccionId();
        trabajadorCriteria.distinct();
    }

    private static Condition<TrabajadorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdUsuario()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getApellido()) &&
                condition.apply(criteria.getDni()) &&
                condition.apply(criteria.getPuesto()) &&
                condition.apply(criteria.getDisponibilidad()) &&
                condition.apply(criteria.getTurno()) &&
                condition.apply(criteria.getEspecialidadId()) &&
                condition.apply(criteria.getInformeId()) &&
                condition.apply(criteria.getRecetaId()) &&
                condition.apply(criteria.getCitaId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getDireccionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrabajadorCriteria> copyFiltersAre(TrabajadorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdUsuario(), copy.getIdUsuario()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getApellido(), copy.getApellido()) &&
                condition.apply(criteria.getDni(), copy.getDni()) &&
                condition.apply(criteria.getPuesto(), copy.getPuesto()) &&
                condition.apply(criteria.getDisponibilidad(), copy.getDisponibilidad()) &&
                condition.apply(criteria.getTurno(), copy.getTurno()) &&
                condition.apply(criteria.getEspecialidadId(), copy.getEspecialidadId()) &&
                condition.apply(criteria.getInformeId(), copy.getInformeId()) &&
                condition.apply(criteria.getRecetaId(), copy.getRecetaId()) &&
                condition.apply(criteria.getCitaId(), copy.getCitaId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getDireccionId(), copy.getDireccionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
