package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InformeCriteriaTest {

    @Test
    void newInformeCriteriaHasAllFiltersNullTest() {
        var informeCriteria = new InformeCriteria();
        assertThat(informeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void informeCriteriaFluentMethodsCreatesFiltersTest() {
        var informeCriteria = new InformeCriteria();

        setAllFilters(informeCriteria);

        assertThat(informeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void informeCriteriaCopyCreatesNullFilterTest() {
        var informeCriteria = new InformeCriteria();
        var copy = informeCriteria.copy();

        assertThat(informeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(informeCriteria)
        );
    }

    @Test
    void informeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var informeCriteria = new InformeCriteria();
        setAllFilters(informeCriteria);

        var copy = informeCriteria.copy();

        assertThat(informeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(informeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var informeCriteria = new InformeCriteria();

        assertThat(informeCriteria).hasToString("InformeCriteria{}");
    }

    private static void setAllFilters(InformeCriteria informeCriteria) {
        informeCriteria.id();
        informeCriteria.fecha();
        informeCriteria.resumen();
        informeCriteria.recetaId();
        informeCriteria.pacienteId();
        informeCriteria.trabajadorId();
        informeCriteria.enfermedadId();
        informeCriteria.citaId();
        informeCriteria.distinct();
    }

    private static Condition<InformeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFecha()) &&
                condition.apply(criteria.getResumen()) &&
                condition.apply(criteria.getRecetaId()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId()) &&
                condition.apply(criteria.getEnfermedadId()) &&
                condition.apply(criteria.getCitaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InformeCriteria> copyFiltersAre(InformeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFecha(), copy.getFecha()) &&
                condition.apply(criteria.getResumen(), copy.getResumen()) &&
                condition.apply(criteria.getRecetaId(), copy.getRecetaId()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId(), copy.getTrabajadorId()) &&
                condition.apply(criteria.getEnfermedadId(), copy.getEnfermedadId()) &&
                condition.apply(criteria.getCitaId(), copy.getCitaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
