package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Paciente} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PacienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pacientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter apellido;

    private StringFilter dni;

    private StringFilter seguroMedico;

    private LocalDateFilter fechaNacimiento;

    private StringFilter telefono;

    private LongFilter trabajadorId;

    private LongFilter citaId;

    private LongFilter informeId;

    private LongFilter recetaId;

    private LongFilter direccionId;

    private Boolean distinct;

    public PacienteCriteria() {}

    public PacienteCriteria(PacienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.apellido = other.optionalApellido().map(StringFilter::copy).orElse(null);
        this.dni = other.optionalDni().map(StringFilter::copy).orElse(null);
        this.seguroMedico = other.optionalSeguroMedico().map(StringFilter::copy).orElse(null);
        this.fechaNacimiento = other.optionalFechaNacimiento().map(LocalDateFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.trabajadorId = other.optionalTrabajadorId().map(LongFilter::copy).orElse(null);
        this.citaId = other.optionalCitaId().map(LongFilter::copy).orElse(null);
        this.informeId = other.optionalInformeId().map(LongFilter::copy).orElse(null);
        this.recetaId = other.optionalRecetaId().map(LongFilter::copy).orElse(null);
        this.direccionId = other.optionalDireccionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PacienteCriteria copy() {
        return new PacienteCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getApellido() {
        return apellido;
    }

    public Optional<StringFilter> optionalApellido() {
        return Optional.ofNullable(apellido);
    }

    public StringFilter apellido() {
        if (apellido == null) {
            setApellido(new StringFilter());
        }
        return apellido;
    }

    public void setApellido(StringFilter apellido) {
        this.apellido = apellido;
    }

    public StringFilter getDni() {
        return dni;
    }

    public Optional<StringFilter> optionalDni() {
        return Optional.ofNullable(dni);
    }

    public StringFilter dni() {
        if (dni == null) {
            setDni(new StringFilter());
        }
        return dni;
    }

    public void setDni(StringFilter dni) {
        this.dni = dni;
    }

    public StringFilter getSeguroMedico() {
        return seguroMedico;
    }

    public Optional<StringFilter> optionalSeguroMedico() {
        return Optional.ofNullable(seguroMedico);
    }

    public StringFilter seguroMedico() {
        if (seguroMedico == null) {
            setSeguroMedico(new StringFilter());
        }
        return seguroMedico;
    }

    public void setSeguroMedico(StringFilter seguroMedico) {
        this.seguroMedico = seguroMedico;
    }

    public LocalDateFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Optional<LocalDateFilter> optionalFechaNacimiento() {
        return Optional.ofNullable(fechaNacimiento);
    }

    public LocalDateFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            setFechaNacimiento(new LocalDateFilter());
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDateFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
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

    public LongFilter getDireccionId() {
        return direccionId;
    }

    public Optional<LongFilter> optionalDireccionId() {
        return Optional.ofNullable(direccionId);
    }

    public LongFilter direccionId() {
        if (direccionId == null) {
            setDireccionId(new LongFilter());
        }
        return direccionId;
    }

    public void setDireccionId(LongFilter direccionId) {
        this.direccionId = direccionId;
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
        final PacienteCriteria that = (PacienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(seguroMedico, that.seguroMedico) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(trabajadorId, that.trabajadorId) &&
            Objects.equals(citaId, that.citaId) &&
            Objects.equals(informeId, that.informeId) &&
            Objects.equals(recetaId, that.recetaId) &&
            Objects.equals(direccionId, that.direccionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            apellido,
            dni,
            seguroMedico,
            fechaNacimiento,
            telefono,
            trabajadorId,
            citaId,
            informeId,
            recetaId,
            direccionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalApellido().map(f -> "apellido=" + f + ", ").orElse("") +
            optionalDni().map(f -> "dni=" + f + ", ").orElse("") +
            optionalSeguroMedico().map(f -> "seguroMedico=" + f + ", ").orElse("") +
            optionalFechaNacimiento().map(f -> "fechaNacimiento=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalTrabajadorId().map(f -> "trabajadorId=" + f + ", ").orElse("") +
            optionalCitaId().map(f -> "citaId=" + f + ", ").orElse("") +
            optionalInformeId().map(f -> "informeId=" + f + ", ").orElse("") +
            optionalRecetaId().map(f -> "recetaId=" + f + ", ").orElse("") +
            optionalDireccionId().map(f -> "direccionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
