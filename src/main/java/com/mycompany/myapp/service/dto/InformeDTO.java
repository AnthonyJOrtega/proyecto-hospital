package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Informe} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformeDTO implements Serializable {

    private Long id;

    private String fecha;

    private String resumen;

    private RecetaDTO receta;

    private PacienteDTO paciente;

    private TrabajadorDTO trabajador;

    private Set<EnfermedadDTO> enfermedads = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public RecetaDTO getReceta() {
        return receta;
    }

    public void setReceta(RecetaDTO receta) {
        this.receta = receta;
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

    public Set<EnfermedadDTO> getEnfermedads() {
        return enfermedads;
    }

    public void setEnfermedads(Set<EnfermedadDTO> enfermedads) {
        this.enfermedads = enfermedads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformeDTO)) {
            return false;
        }

        InformeDTO informeDTO = (InformeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, informeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformeDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", resumen='" + getResumen() + "'" +
            ", receta=" + getReceta() +
            ", paciente=" + getPaciente() +
            ", trabajador=" + getTrabajador() +
            ", enfermedads=" + getEnfermedads() +
            "}";
    }
}
