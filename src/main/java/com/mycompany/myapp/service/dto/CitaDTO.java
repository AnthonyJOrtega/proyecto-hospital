package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.EstadoCita;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Cita} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CitaDTO implements Serializable {

    private Long id;

    private LocalDate fechaCreacion;

    private LocalTime horaCreacion;

    private EstadoCita estadoCita;

    private EstadoCita estadoPaciente;

    private String observaciones;

    private InformeDTO informe;

    private PacienteDTO paciente;

    private Set<TrabajadorDTO> trabajadors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalTime getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(LocalTime horaCreacion) {
        this.horaCreacion = horaCreacion;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }

    public EstadoCita getEstadoPaciente() {
        return estadoPaciente;
    }

    public void setEstadoPaciente(EstadoCita estadoPaciente) {
        this.estadoPaciente = estadoPaciente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public InformeDTO getInforme() {
        return informe;
    }

    public void setInforme(InformeDTO informe) {
        this.informe = informe;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public Set<TrabajadorDTO> getTrabajadors() {
        return trabajadors;
    }

    public void setTrabajadors(Set<TrabajadorDTO> trabajadors) {
        this.trabajadors = trabajadors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CitaDTO)) {
            return false;
        }

        CitaDTO citaDTO = (CitaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, citaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CitaDTO{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", horaCreacion='" + getHoraCreacion() + "'" +
            ", estadoCita='" + getEstadoCita() + "'" +
            ", estadoPaciente='" + getEstadoPaciente() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", informe=" + getInforme() +
            ", paciente=" + getPaciente() +
            ", trabajadors=" + getTrabajadors() +
            "}";
    }
}
