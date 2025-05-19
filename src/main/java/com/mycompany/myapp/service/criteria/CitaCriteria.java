package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.EstadoCita;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Cita} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CitaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /citas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoCita
     */
    public static class EstadoCitaFilter extends Filter<EstadoCita> {

        public EstadoCitaFilter() {}

        public EstadoCitaFilter(EstadoCitaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoCitaFilter copy() {
            return new EstadoCitaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter fechaCreacion;

    private EstadoCitaFilter estadoCita;

    private StringFilter observaciones;

    private LongFilter informeId;

    private LongFilter pacienteId;

    private LongFilter trabajadorId;

    private Boolean distinct;

    public CitaCriteria() {}

    public CitaCriteria(CitaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fechaCreacion = other.optionalFechaCreacion().map(LocalDateFilter::copy).orElse(null);
        this.estadoCita = other.optionalEstadoCita().map(EstadoCitaFilter::copy).orElse(null);
        this.observaciones = other.optionalObservaciones().map(StringFilter::copy).orElse(null);
        this.informeId = other.optionalInformeId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.trabajadorId = other.optionalTrabajadorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CitaCriteria copy() {
        return new CitaCriteria(this);
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

    public LocalDateFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public Optional<LocalDateFilter> optionalFechaCreacion() {
        return Optional.ofNullable(fechaCreacion);
    }

    public LocalDateFilter fechaCreacion() {
        if (fechaCreacion == null) {
            setFechaCreacion(new LocalDateFilter());
        }
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoCitaFilter getEstadoCita() {
        return estadoCita;
    }

    public Optional<EstadoCitaFilter> optionalEstadoCita() {
        return Optional.ofNullable(estadoCita);
    }

    public EstadoCitaFilter estadoCita() {
        if (estadoCita == null) {
            setEstadoCita(new EstadoCitaFilter());
        }
        return estadoCita;
    }

    public void setEstadoCita(EstadoCitaFilter estadoCita) {
        this.estadoCita = estadoCita;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public Optional<StringFilter> optionalObservaciones() {
        return Optional.ofNullable(observaciones);
    }

    public StringFilter observaciones() {
        if (observaciones == null) {
            setObservaciones(new StringFilter());
        }
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
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
        final CitaCriteria that = (CitaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(estadoCita, that.estadoCita) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(informeId, that.informeId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(trabajadorId, that.trabajadorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fechaCreacion, estadoCita, observaciones, informeId, pacienteId, trabajadorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFechaCreacion().map(f -> "fechaCreacion=" + f + ", ").orElse("") +
            optionalEstadoCita().map(f -> "estadoCita=" + f + ", ").orElse("") +
            optionalObservaciones().map(f -> "observaciones=" + f + ", ").orElse("") +
            optionalInformeId().map(f -> "informeId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalTrabajadorId().map(f -> "trabajadorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
