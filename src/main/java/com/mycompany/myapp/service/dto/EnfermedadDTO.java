package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Enfermedad} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnfermedadDTO implements Serializable {

    private Long id;

    private String nombre;

    private String descripcion;

    private Set<InformeDTO> informes = new HashSet<>();

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

    public Set<InformeDTO> getInformes() {
        return informes;
    }

    public void setInformes(Set<InformeDTO> informes) {
        this.informes = informes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnfermedadDTO)) {
            return false;
        }

        EnfermedadDTO enfermedadDTO = (EnfermedadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enfermedadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnfermedadDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", informes=" + getInformes() +
            "}";
    }
}
