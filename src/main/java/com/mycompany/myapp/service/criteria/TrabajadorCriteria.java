package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Puesto;
import com.mycompany.myapp.domain.enumeration.Turno;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Trabajador} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TrabajadorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trabajadors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrabajadorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Puesto
     */
    public static class PuestoFilter extends Filter<Puesto> {

        public PuestoFilter() {}

        public PuestoFilter(PuestoFilter filter) {
            super(filter);
        }

        @Override
        public PuestoFilter copy() {
            return new PuestoFilter(this);
        }
    }

    /**
     * Class for filtering Turno
     */
    public static class TurnoFilter extends Filter<Turno> {

        public TurnoFilter() {}

        public TurnoFilter(TurnoFilter filter) {
            super(filter);
        }

        @Override
        public TurnoFilter copy() {
            return new TurnoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter idUsuario;

    private StringFilter nombre;

    private StringFilter apellido;

    private StringFilter dni;

    private PuestoFilter puesto;

    private BooleanFilter disponibilidad;

    private TurnoFilter turno;

    private LongFilter especialidadId;

    private LongFilter informeId;

    private LongFilter recetaId;

    private LongFilter citaId;

    private LongFilter pacienteId;

    private LongFilter direccionId;

    private Boolean distinct;

    public TrabajadorCriteria() {}

    public TrabajadorCriteria(TrabajadorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.idUsuario = other.optionalIdUsuario().map(LongFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.apellido = other.optionalApellido().map(StringFilter::copy).orElse(null);
        this.dni = other.optionalDni().map(StringFilter::copy).orElse(null);
        this.puesto = other.optionalPuesto().map(PuestoFilter::copy).orElse(null);
        this.disponibilidad = other.optionalDisponibilidad().map(BooleanFilter::copy).orElse(null);
        this.turno = other.optionalTurno().map(TurnoFilter::copy).orElse(null);
        this.especialidadId = other.optionalEspecialidadId().map(LongFilter::copy).orElse(null);
        this.informeId = other.optionalInformeId().map(LongFilter::copy).orElse(null);
        this.recetaId = other.optionalRecetaId().map(LongFilter::copy).orElse(null);
        this.citaId = other.optionalCitaId().map(LongFilter::copy).orElse(null);
        this.pacienteId = other.optionalPacienteId().map(LongFilter::copy).orElse(null);
        this.direccionId = other.optionalDireccionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrabajadorCriteria copy() {
        return new TrabajadorCriteria(this);
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

    public LongFilter getIdUsuario() {
        return idUsuario;
    }

    public Optional<LongFilter> optionalIdUsuario() {
        return Optional.ofNullable(idUsuario);
    }

    public LongFilter idUsuario() {
        if (idUsuario == null) {
            setIdUsuario(new LongFilter());
        }
        return idUsuario;
    }

    public void setIdUsuario(LongFilter idUsuario) {
        this.idUsuario = idUsuario;
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

    public PuestoFilter getPuesto() {
        return puesto;
    }

    public Optional<PuestoFilter> optionalPuesto() {
        return Optional.ofNullable(puesto);
    }

    public PuestoFilter puesto() {
        if (puesto == null) {
            setPuesto(new PuestoFilter());
        }
        return puesto;
    }

    public void setPuesto(PuestoFilter puesto) {
        this.puesto = puesto;
    }

    public BooleanFilter getDisponibilidad() {
        return disponibilidad;
    }

    public Optional<BooleanFilter> optionalDisponibilidad() {
        return Optional.ofNullable(disponibilidad);
    }

    public BooleanFilter disponibilidad() {
        if (disponibilidad == null) {
            setDisponibilidad(new BooleanFilter());
        }
        return disponibilidad;
    }

    public void setDisponibilidad(BooleanFilter disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public TurnoFilter getTurno() {
        return turno;
    }

    public Optional<TurnoFilter> optionalTurno() {
        return Optional.ofNullable(turno);
    }

    public TurnoFilter turno() {
        if (turno == null) {
            setTurno(new TurnoFilter());
        }
        return turno;
    }

    public void setTurno(TurnoFilter turno) {
        this.turno = turno;
    }

    public LongFilter getEspecialidadId() {
        return especialidadId;
    }

    public Optional<LongFilter> optionalEspecialidadId() {
        return Optional.ofNullable(especialidadId);
    }

    public LongFilter especialidadId() {
        if (especialidadId == null) {
            setEspecialidadId(new LongFilter());
        }
        return especialidadId;
    }

    public void setEspecialidadId(LongFilter especialidadId) {
        this.especialidadId = especialidadId;
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
        final TrabajadorCriteria that = (TrabajadorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idUsuario, that.idUsuario) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(apellido, that.apellido) &&
            Objects.equals(dni, that.dni) &&
            Objects.equals(puesto, that.puesto) &&
            Objects.equals(disponibilidad, that.disponibilidad) &&
            Objects.equals(turno, that.turno) &&
            Objects.equals(especialidadId, that.especialidadId) &&
            Objects.equals(informeId, that.informeId) &&
            Objects.equals(recetaId, that.recetaId) &&
            Objects.equals(citaId, that.citaId) &&
            Objects.equals(pacienteId, that.pacienteId) &&
            Objects.equals(direccionId, that.direccionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idUsuario,
            nombre,
            apellido,
            dni,
            puesto,
            disponibilidad,
            turno,
            especialidadId,
            informeId,
            recetaId,
            citaId,
            pacienteId,
            direccionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrabajadorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdUsuario().map(f -> "idUsuario=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalApellido().map(f -> "apellido=" + f + ", ").orElse("") +
            optionalDni().map(f -> "dni=" + f + ", ").orElse("") +
            optionalPuesto().map(f -> "puesto=" + f + ", ").orElse("") +
            optionalDisponibilidad().map(f -> "disponibilidad=" + f + ", ").orElse("") +
            optionalTurno().map(f -> "turno=" + f + ", ").orElse("") +
            optionalEspecialidadId().map(f -> "especialidadId=" + f + ", ").orElse("") +
            optionalInformeId().map(f -> "informeId=" + f + ", ").orElse("") +
            optionalRecetaId().map(f -> "recetaId=" + f + ", ").orElse("") +
            optionalCitaId().map(f -> "citaId=" + f + ", ").orElse("") +
            optionalPacienteId().map(f -> "pacienteId=" + f + ", ").orElse("") +
            optionalDireccionId().map(f -> "direccionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
