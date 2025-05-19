package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EnfermedadCriteriaTest {

    @Test
    void newEnfermedadCriteriaHasAllFiltersNullTest() {
        var enfermedadCriteria = new EnfermedadCriteria();
        assertThat(enfermedadCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void enfermedadCriteriaFluentMethodsCreatesFiltersTest() {
        var enfermedadCriteria = new EnfermedadCriteria();

        setAllFilters(enfermedadCriteria);

        assertThat(enfermedadCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void enfermedadCriteriaCopyCreatesNullFilterTest() {
        var enfermedadCriteria = new EnfermedadCriteria();
        var copy = enfermedadCriteria.copy();

        assertThat(enfermedadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(enfermedadCriteria)
        );
    }

    @Test
    void enfermedadCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var enfermedadCriteria = new EnfermedadCriteria();
        setAllFilters(enfermedadCriteria);

        var copy = enfermedadCriteria.copy();

        assertThat(enfermedadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(enfermedadCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var enfermedadCriteria = new EnfermedadCriteria();

        assertThat(enfermedadCriteria).hasToString("EnfermedadCriteria{}");
    }

    private static void setAllFilters(EnfermedadCriteria enfermedadCriteria) {
        enfermedadCriteria.id();
        enfermedadCriteria.nombre();
        enfermedadCriteria.descripcion();
        enfermedadCriteria.informeId();
        enfermedadCriteria.distinct();
    }

    private static Condition<EnfermedadCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getInformeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EnfermedadCriteria> copyFiltersAre(EnfermedadCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getInformeId(), copy.getInformeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
