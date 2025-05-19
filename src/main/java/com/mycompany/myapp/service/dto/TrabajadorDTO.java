package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Puesto;
import com.mycompany.myapp.domain.enumeration.Turno;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Trabajador} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrabajadorDTO implements Serializable {

    private Long id;

    private Long idUsuario;

    private String nombre;

    private String apellido;

    private String dni;

    private Puesto puesto;

    private Boolean disponibilidad;

    private Turno turno;

    private Set<EspecialidadDTO> especialidads = new HashSet<>();

    private Set<CitaDTO> citas = new HashSet<>();

    private Set<PacienteDTO> pacientes = new HashSet<>();

    private Set<DireccionDTO> direccions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Set<EspecialidadDTO> getEspecialidads() {
        return especialidads;
    }

    public void setEspecialidads(Set<EspecialidadDTO> especialidads) {
        this.especialidads = especialidads;
    }

    public Set<CitaDTO> getCitas() {
        return citas;
    }

    public void setCitas(Set<CitaDTO> citas) {
        this.citas = citas;
    }

    public Set<PacienteDTO> getPacientes() {
        return pacientes;
    }

    public void setPacientes(Set<PacienteDTO> pacientes) {
        this.pacientes = pacientes;
    }

    public Set<DireccionDTO> getDireccions() {
        return direccions;
    }

    public void setDireccions(Set<DireccionDTO> direccions) {
        this.direccions = direccions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrabajadorDTO)) {
            return false;
        }

        TrabajadorDTO trabajadorDTO = (TrabajadorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trabajadorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrabajadorDTO{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni='" + getDni() + "'" +
            ", puesto='" + getPuesto() + "'" +
            ", disponibilidad='" + getDisponibilidad() + "'" +
            ", turno='" + getTurno() + "'" +
            ", especialidads=" + getEspecialidads() +
            ", citas=" + getCitas() +
            ", pacientes=" + getPacientes() +
            ", direccions=" + getDireccions() +
            "}";
    }
}
