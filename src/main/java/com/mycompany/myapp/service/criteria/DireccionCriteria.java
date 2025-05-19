package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Direccion} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.DireccionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /direccions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DireccionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pais;

    private StringFilter ciudad;

    private StringFilter localidad;

    private IntegerFilter codigoPostal;

    private StringFilter calle;

    private StringFilter numero;

    private LongFilter pacienteId;

    private LongFilter trabajadorId;

    private Boolean distinct;

    public DireccionCriteria() {}

    public DireccionCriteria(DireccionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pais = other.optionalPais().map(StringFilter::copy).orElse(null);
        this.ciudad = other.optionalCiudad().map(StringFilter::copy).orElse(null);
        this.localidad = other.optionalLocalidad().map(StringFilter::copy).orElse(null);
        this.codigoPostal = other.optionalCodigoPostal().map(IntegerFilter::copy).orElse(null);
        this.calle = other.optionalCalle().map(StringFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(StringFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.trabajadorId = other.optionalTrabajadorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DireccionCriteria copy() {
        return new DireccionCriteria(this);
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

    public StringFilter getPais() {
        return pais;
    }

    public Optional<StringFilter> optionalPais() {
        return Optional.ofNullable(pais);
    }

    public StringFilter pais() {
        if (pais == null) {
            setPais(new StringFilter());
        }
        return pais;
    }

    public void setPais(StringFilter pais) {
        this.pais = pais;
    }

    public StringFilter getCiudad() {
        return ciudad;
    }

    public Optional<StringFilter> optionalCiudad() {
        return Optional.ofNullable(ciudad);
    }

    public StringFilter ciudad() {
        if (ciudad == null) {
            setCiudad(new StringFilter());
        }
        return ciudad;
    }

    public void setCiudad(StringFilter ciudad) {
        this.ciudad = ciudad;
    }

    public StringFilter getLocalidad() {
        return localidad;
    }

    public Optional<StringFilter> optionalLocalidad() {
        return Optional.ofNullable(localidad);
    }

    public StringFilter localidad() {
        if (localidad == null) {
            setLocalidad(new StringFilter());
        }
        return localidad;
    }

    public void setLocalidad(StringFilter localidad) {
        this.localidad = localidad;
    }

    public IntegerFilter getCodigoPostal() {
        return codigoPostal;
    }

    public Optional<IntegerFilter> optionalCodigoPostal() {
        return Optional.ofNullable(codigoPostal);
    }

    public IntegerFilter codigoPostal() {
        if (codigoPostal == null) {
            setCodigoPostal(new IntegerFilter());
        }
        return codigoPostal;
    }

    public void setCodigoPostal(IntegerFilter codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public StringFilter getCalle() {
        return calle;
    }

    public Optional<StringFilter> optionalCalle() {
        return Optional.ofNullable(calle);
    }

    public StringFilter calle() {
        if (calle == null) {
            setCalle(new StringFilter());
        }
        return calle;
    }

    public void setCalle(StringFilter calle) {
        this.calle = calle;
    }

    public StringFilter getNumero() {
        return numero;
    }

    public Optional<StringFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public StringFilter numero() {
        if (numero == null) {
            setNumero(new StringFilter());
        }
        return numero;
    }

    public void setNumero(StringFilter numero) {
        this.numero = numero;
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
        final DireccionCriteria that = (DireccionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pais, that.pais) &&
            Objects.equals(ciudad, that.ciudad) &&
            Objects.equals(localidad, that.localidad) &&
            Objects.equals(codigoPostal, that.codigoPostal) &&
            Objects.equals(calle, that.calle) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(trabajadorId, that.trabajadorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pais, ciudad, localidad, codigoPostal, calle, numero, pacienteId, trabajadorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DireccionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPais().map(f -> "pais=" + f + ", ").orElse("") +
            optionalCiudad().map(f -> "ciudad=" + f + ", ").orElse("") +
            optionalLocalidad().map(f -> "localidad=" + f + ", ").orElse("") +
            optionalCodigoPostal().map(f -> "codigoPostal=" + f + ", ").orElse("") +
            optionalCalle().map(f -> "calle=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalTrabajadorId().map(f -> "trabajadorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
