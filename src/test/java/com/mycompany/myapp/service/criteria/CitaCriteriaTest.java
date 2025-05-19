package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CitaCriteriaTest {

    @Test
    void newCitaCriteriaHasAllFiltersNullTest() {
        var citaCriteria = new CitaCriteria();
        assertThat(citaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void citaCriteriaFluentMethodsCreatesFiltersTest() {
        var citaCriteria = new CitaCriteria();

        setAllFilters(citaCriteria);

        assertThat(citaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void citaCriteriaCopyCreatesNullFilterTest() {
        var citaCriteria = new CitaCriteria();
        var copy = citaCriteria.copy();

        assertThat(citaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(citaCriteria)
        );
    }

    @Test
    void citaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var citaCriteria = new CitaCriteria();
        setAllFilters(citaCriteria);

        var copy = citaCriteria.copy();

        assertThat(citaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(citaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var citaCriteria = new CitaCriteria();

        assertThat(citaCriteria).hasToString("CitaCriteria{}");
    }

    private static void setAllFilters(CitaCriteria citaCriteria) {
        citaCriteria.id();
        citaCriteria.fechaCreacion();
        citaCriteria.estadoCita();
        citaCriteria.observaciones();
        citaCriteria.informeId();
        citaCriteria.pacienteId();
        citaCriteria.trabajadorId();
        citaCriteria.distinct();
    }

    private static Condition<CitaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaCreacion()) &&
                condition.apply(criteria.getEstadoCita()) &&
                condition.apply(criteria.getObservaciones()) &&
                condition.apply(criteria.getInformeId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CitaCriteria> copyFiltersAre(CitaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaCreacion(), copy.getFechaCreacion()) &&
                condition.apply(criteria.getEstadoCita(), copy.getEstadoCita()) &&
                condition.apply(criteria.getObservaciones(), copy.getObservaciones()) &&
                condition.apply(criteria.getInformeId(), copy.getInformeId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId(), copy.getTrabajadorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
