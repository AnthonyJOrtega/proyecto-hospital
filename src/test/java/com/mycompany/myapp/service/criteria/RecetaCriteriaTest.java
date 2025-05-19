package com.mycompany.myapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RecetaCriteriaTest {

    @Test
    void newRecetaCriteriaHasAllFiltersNullTest() {
        var recetaCriteria = new RecetaCriteria();
        assertThat(recetaCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recetaCriteriaFluentMethodsCreatesFiltersTest() {
        var recetaCriteria = new RecetaCriteria();

        setAllFilters(recetaCriteria);

        assertThat(recetaCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recetaCriteriaCopyCreatesNullFilterTest() {
        var recetaCriteria = new RecetaCriteria();
        var copy = recetaCriteria.copy();

        assertThat(recetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recetaCriteria)
        );
    }

    @Test
    void recetaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recetaCriteria = new RecetaCriteria();
        setAllFilters(recetaCriteria);

        var copy = recetaCriteria.copy();

        assertThat(recetaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recetaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recetaCriteria = new RecetaCriteria();

        assertThat(recetaCriteria).hasToString("RecetaCriteria{}");
    }

    private static void setAllFilters(RecetaCriteria recetaCriteria) {
        recetaCriteria.id();
        recetaCriteria.fechaInicio();
        recetaCriteria.fechaFin();
        recetaCriteria.instrucciones();
        recetaCriteria.pacienteId();
        recetaCriteria.trabajadorId();
        recetaCriteria.medicamentoId();
        recetaCriteria.informeId();
        recetaCriteria.distinct();
    }

    private static Condition<RecetaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin()) &&
                condition.apply(criteria.getInstrucciones()) &&
                condition.apply(criteria.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId()) &&
                condition.apply(criteria.getMedicamentoId()) &&
                condition.apply(criteria.getInformeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecetaCriteria> copyFiltersAre(RecetaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFechaInicio(), copy.getFechaInicio()) &&
                condition.apply(criteria.getFechaFin(), copy.getFechaFin()) &&
                condition.apply(criteria.getInstrucciones(), copy.getInstrucciones()) &&
                condition.apply(criteria.getPacienteId(), copy.getPacienteId()) &&
                condition.apply(criteria.getTrabajadorId(), copy.getTrabajadorId()) &&
                condition.apply(criteria.getMedicamentoId(), copy.getMedicamentoId()) &&
                condition.apply(criteria.getInformeId(), copy.getInformeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
