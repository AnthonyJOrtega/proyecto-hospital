package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DireccionCriteriaTest {

    @Test
    void newDireccionCriteriaHasAllFiltersNullTest() {
        var direccionCriteria = new DireccionCriteria();
        assertThat(direccionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void direccionCriteriaFluentMethodsCreatesFiltersTest() {
        var direccionCriteria = new DireccionCriteria();

        setAllFilters(direccionCriteria);

        assertThat(direccionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void direccionCriteriaCopyCreatesNullFilterTest() {
        var direccionCriteria = new DireccionCriteria();
        var copy = direccionCriteria.copy();

        assertThat(direccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(direccionCriteria)
        );
    }

    @Test
    void direccionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var direccionCriteria = new DireccionCriteria();
        setAllFilters(direccionCriteria);

        var copy = direccionCriteria.copy();

        assertThat(direccionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(direccionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var direccionCriteria = new DireccionCriteria();

        assertThat(direccionCriteria).hasToString("DireccionCriteria{}");
    }

    private static void setAllFilters(DireccionCriteria direccionCriteria) {
        direccionCriteria.id();
        direccionCriteria.pais();
        direccionCriteria.ciudad();
        direccionCriteria.localidad();
        direccionCriteria.codigoPostal();
        direccionCriteria.calle();
        direccionCriteria.numero();
        direccionCriteria.pacienteId();
        direccionCriteria.trabajadorId();
        direccionCriteria.distinct();
    }

    private static Condition<DireccionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPais()) &&
                condition.apply(criteria.getCiudad()) &&
                condition.apply(criteria.getLocalidad()) &&
                condition.apply(criteria.getCodigoPostal()) &&
                condition.apply(criteria.getCalle()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DireccionCriteria> copyFiltersAre(DireccionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPais(), copy.getPais()) &&
                condition.apply(criteria.getCiudad(), copy.getCiudad()) &&
                condition.apply(criteria.getLocalidad(), copy.getLocalidad()) &&
                condition.apply(criteria.getCodigoPostal(), copy.getCodigoPostal()) &&
                condition.apply(criteria.getCalle(), copy.getCalle()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId(), copy.getTrabajadorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
