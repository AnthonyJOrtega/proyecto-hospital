package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Receta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecetaDTO implements Serializable {

    private Long id;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String instrucciones;

    private PacienteDTO paciente;

    private TrabajadorDTO trabajador;

    private InformeDTO informe;

    private Set<MedicamentoDTO> medicamentos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public PacienteDTO getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteDTO paciente) {
        this.paciente = paciente;
    }

    public TrabajadorDTO getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(TrabajadorDTO trabajador) {
        this.trabajador = trabajador;
    }

    public InformeDTO getInforme() {
        return informe;
    }

    public void setInforme(InformeDTO informe) {
        this.informe = informe;
    }

    public Set<MedicamentoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(Set<MedicamentoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecetaDTO)) {
            return false;
        }

        RecetaDTO recetaDTO = (RecetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, recetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecetaDTO{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", instrucciones='" + getInstrucciones() + "'" +
            ", paciente=" + getPaciente() +
            ", trabajador=" + getTrabajador() +
            ", medicamentos=" + getMedicamentos() +
            "}";
    }
}
