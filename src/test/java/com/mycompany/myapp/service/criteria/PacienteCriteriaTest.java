package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PacienteCriteriaTest {

    @Test
    void newPacienteCriteriaHasAllFiltersNullTest() {
        var pacienteCriteria = new PacienteCriteria();
        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pacienteCriteriaFluentMethodsCreatesFiltersTest() {
        var pacienteCriteria = new PacienteCriteria();

        setAllFilters(pacienteCriteria);

        assertThat(pacienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pacienteCriteriaCopyCreatesNullFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void pacienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pacienteCriteria = new PacienteCriteria();
        setAllFilters(pacienteCriteria);

        var copy = pacienteCriteria.copy();

        assertThat(pacienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pacienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pacienteCriteria = new PacienteCriteria();

        assertThat(pacienteCriteria).hasToString("PacienteCriteria{}");
    }

    private static void setAllFilters(PacienteCriteria pacienteCriteria) {
        pacienteCriteria.id();
        pacienteCriteria.nombre();
        pacienteCriteria.apellido();
        pacienteCriteria.dni();
        pacienteCriteria.seguroMedico();
        pacienteCriteria.fechaNacimiento();
        pacienteCriteria.telefono();
        pacienteCriteria.trabajadorId();
        pacienteCriteria.citaId();
        pacienteCriteria.informeId();
        pacienteCriteria.recetaId();
        pacienteCriteria.direccionId();
        pacienteCriteria.distinct();
    }

    private static Condition<PacienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getApellido()) &&
                condition.apply(criteria.getDni()) &&
                condition.apply(criteria.getSeguroMedico()) &&
                condition.apply(criteria.getFechaNacimiento()) &&
                condition.apply(criteria.getTelefono()) &&
                condition.apply(criteria.getTrabajadorId()) &&
                condition.apply(criteria.getCitaId()) &&
                condition.apply(criteria.getInformeId()) &&
                condition.apply(criteria.getRecetaId()) &&
                condition.apply(criteria.getDireccionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PacienteCriteria> copyFiltersAre(PacienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getApellido(), copy.getApellido()) &&
                condition.apply(criteria.getDni(), copy.getDni()) &&
                condition.apply(criteria.getSeguroMedico(), copy.getSeguroMedico()) &&
                condition.apply(criteria.getFechaNacimiento(), copy.getFechaNacimiento()) &&
                condition.apply(criteria.getTelefono(), copy.getTelefono()) &&
                condition.apply(criteria.getTrabajadorId(), copy.getTrabajadorId()) &&
                condition.apply(criteria.getCitaId(), copy.getCitaId()) &&
                condition.apply(criteria.getInformeId(), copy.getInformeId()) &&
                condition.apply(criteria.getRecetaId(), copy.getRecetaId()) &&
                condition.apply(criteria.getDireccionId(), copy.getDireccionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
