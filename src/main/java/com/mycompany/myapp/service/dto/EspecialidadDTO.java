package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Especialidad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EspecialidadDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private Set<TrabajadorDTO> trabajadors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(o instanceof EspecialidadDTO)) {
            return false;
        }

        EspecialidadDTO especialidadDTO = (EspecialidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, especialidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EspecialidadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", trabajadors=" + getTrabajadors() +
            "}";
    }
}
