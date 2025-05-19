package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Informe} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.InformeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /informes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fecha;

    private StringFilter resumen;

    private LongFilter recetaId;

    private LongFilter pacienteId;

    private LongFilter trabajadorId;

    private LongFilter enfermedadId;

    private LongFilter citaId;

    private Boolean distinct;

    public InformeCriteria() {}

    public InformeCriteria(InformeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fecha = other.optionalFecha().map(StringFilter::copy).orElse(null);
        this.resumen = other.optionalResumen().map(StringFilter::copy).orElse(null);
        this.recetaId = other.optionalRecetaId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.trabajadorId = other.optionalTrabajadorId().map(LongFilter::copy).orElse(null);
        this.enfermedadId = other.optionalEnfermedadId().map(LongFilter::copy).orElse(null);
        this.citaId = other.optionalCitaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InformeCriteria copy() {
        return new InformeCriteria(this);
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

    public StringFilter getFecha() {
        return fecha;
    }

    public Optional<StringFilter> optionalFecha() {
        return Optional.ofNullable(fecha);
    }

    public StringFilter fecha() {
        if (fecha == null) {
            setFecha(new StringFilter());
        }
        return fecha;
    }

    public void setFecha(StringFilter fecha) {
        this.fecha = fecha;
    }

    public StringFilter getResumen() {
        return resumen;
    }

    public Optional<StringFilter> optionalResumen() {
        return Optional.ofNullable(resumen);
    }

    public StringFilter resumen() {
        if (resumen == null) {
            setResumen(new StringFilter());
        }
        return resumen;
    }

    public void setResumen(StringFilter resumen) {
        this.resumen = resumen;
    }

    public LongFilter getRecetaId() {
        return recetaId;
    }

    public Optional<LongFilter> optionalRecetaId() {
        return Optional.ofNullable(recetaId);
    }

    public LongFilter recetaId() {
        if (recetaId == null) {
            setRecetaId(new LongFilter());
        }
        return recetaId;
    }

    public void setRecetaId(LongFilter recetaId) {
        this.recetaId = recetaId;
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

    public LongFilter getEnfermedadId() {
        return enfermedadId;
    }

    public Optional<LongFilter> optionalEnfermedadId() {
        return Optional.ofNullable(enfermedadId);
    }

    public LongFilter enfermedadId() {
        if (enfermedadId == null) {
            setEnfermedadId(new LongFilter());
        }
        return enfermedadId;
    }

    public void setEnfermedadId(LongFilter enfermedadId) {
        this.enfermedadId = enfermedadId;
    }

    public LongFilter getCitaId() {
        return citaId;
    }

    public Optional<LongFilter> optionalCitaId() {
        return Optional.ofNullable(citaId);
    }

    public LongFilter citaId() {
        if (citaId == null) {
            setCitaId(new LongFilter());
        }
        return citaId;
    }

    public void setCitaId(LongFilter citaId) {
        this.citaId = citaId;
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
        final InformeCriteria that = (InformeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fecha, that.fecha) &&
            Objects.equals(resumen, that.resumen) &&
            Objects.equals(recetaId, that.recetaId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(trabajadorId, that.trabajadorId) &&
            Objects.equals(enfermedadId, that.enfermedadId) &&
            Objects.equals(citaId, that.citaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, resumen, recetaId, pacienteId, trabajadorId, enfermedadId, citaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFecha().map(f -> "fecha=" + f + ", ").orElse("") +
            optionalResumen().map(f -> "resumen=" + f + ", ").orElse("") +
            optionalRecetaId().map(f -> "recetaId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalTrabajadorId().map(f -> "trabajadorId=" + f + ", ").orElse("") +
            optionalEnfermedadId().map(f -> "enfermedadId=" + f + ", ").orElse("") +
            optionalCitaId().map(f -> "citaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
