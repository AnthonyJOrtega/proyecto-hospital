package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Receta} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.RecetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recetas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaInicio;

    private LocalDateFilter fechaFin;

    private StringFilter instrucciones;

    private LongFilter pacienteId;

    private LongFilter trabajadorId;

    private LongFilter medicamentoId;

    private LongFilter informeId;

    private Boolean distinct;

    public RecetaCriteria() {}

    public RecetaCriteria(RecetaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaInicio = other.optionalFechaInicio().map(LocalDateFilter::copy).orElse(null);
        this.fechaFin = other.optionalFechaFin().map(LocalDateFilter::copy).orElse(null);
        this.instrucciones = other.optionalInstrucciones().map(StringFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.trabajadorId = other.optionalTrabajadorId().map(LongFilter::copy).orElse(null);
        this.medicamentoId = other.optionalMedicamentoId().map(LongFilter::copy).orElse(null);
        this.informeId = other.optionalInformeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecetaCriteria copy() {
        return new RecetaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getFechaInicio() {
        return fechaInicio;
    }

    public Optional<LocalDateFilter> optionalFechaInicio() {
        return Optional.ofNullable(fechaInicio);
    }

    public LocalDateFilter fechaInicio() {
        if (fechaInicio == null) {
            setFechaInicio(new LocalDateFilter());
        }
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateFilter fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateFilter getFechaFin() {
        return fechaFin;
    }

    public Optional<LocalDateFilter> optionalFechaFin() {
        return Optional.ofNullable(fechaFin);
    }

    public LocalDateFilter fechaFin() {
        if (fechaFin == null) {
            setFechaFin(new LocalDateFilter());
        }
        return fechaFin;
    }

    public void setFechaFin(LocalDateFilter fechaFin) {
        this.fechaFin = fechaFin;
    }

    public StringFilter getInstrucciones() {
        return instrucciones;
    }

    public Optional<StringFilter> optionalInstrucciones() {
        return Optional.ofNullable(instrucciones);
    }

    public StringFilter instrucciones() {
        if (instrucciones == null) {
            setInstrucciones(new StringFilter());
        }
        return instrucciones;
    }

    public void setInstrucciones(StringFilter instrucciones) {
        this.instrucciones = instrucciones;
    }

    public LongFilter getPacienteId() {
        return pacienteId;
    }

    public Optional<LongFilter> optionalPacienteId() {
        return Optional.ofNullable(pacienteId);
    }

    public LongFilter pacienteId() {
        if (pacienteId == null) {
            setPacienteId(new LongFilter());
        }
        return pacienteId;
    }

    public void setPacienteId(LongFilter pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LongFilter getTrabajadorId() {
        return trabajadorId;
    }

    public Optional<LongFilter> optionalTrabajadorId() {
        return Optional.ofNullable(trabajadorId);
    }

    public LongFilter trabajadorId() {
        if (trabajadorId == null) {
            setTrabajadorId(new LongFilter());
        }
        return trabajadorId;
    }

    public void setTrabajadorId(LongFilter trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public LongFilter getMedicamentoId() {
        return medicamentoId;
    }

    public Optional<LongFilter> optionalMedicamentoId() {
        return Optional.ofNullable(medicamentoId);
    }

    public LongFilter medicamentoId() {
        if (medicamentoId == null) {
            setMedicamentoId(new LongFilter());
        }
        return medicamentoId;
    }

    public void setMedicamentoId(LongFilter medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public LongFilter getInformeId() {
        return informeId;
    }

    public Optional<LongFilter> optionalInformeId() {
        return Optional.ofNullable(informeId);
    }

    public LongFilter informeId() {
        if (informeId == null) {
            setInformeId(new LongFilter());
        }
        return informeId;
    }

    public void setInformeId(LongFilter informeId) {
        this.informeId = informeId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecetaCriteria that = (RecetaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaInicio, that.fechaInicio) &&
            Objects.equals(fechaFin, that.fechaFin) &&
            Objects.equals(instrucciones, that.instrucciones) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(trabajadorId, that.trabajadorId) &&
            Objects.equals(medicamentoId, that.medicamentoId) &&
            Objects.equals(informeId, that.informeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaInicio, fechaFin, instrucciones, pacienteId, trabajadorId, medicamentoId, informeId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecetaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaInicio().map(f -> "fechaInicio=" + f + ", ").orElse("") +
            optionalFechaFin().map(f -> "fechaFin=" + f + ", ").orElse("") +
            optionalInstrucciones().map(f -> "instrucciones=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalTrabajadorId().map(f -> "trabajadorId=" + f + ", ").orElse("") +
            optionalMedicamentoId().map(f -> "medicamentoId=" + f + ", ").orElse("") +
            optionalInformeId().map(f -> "informeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
