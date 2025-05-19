package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Direccion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DireccionDTO implements Serializable {

    private Long id;

    private String pais;

    private String ciudad;

    private String localidad;

    private Integer codigoPostal;

    private String calle;

    private String numero;

    private Set<PacienteDTO> pacientes = new HashSet<>();

    private Set<TrabajadorDTO> trabajadors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Set<PacienteDTO> getPacientes() {
        return pacientes;
    }

    public void setPacientes(Set<PacienteDTO> pacientes) {
        this.pacientes = pacientes;
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
        if (!(o instanceof DireccionDTO)) {
            return false;
        }

        DireccionDTO direccionDTO = (DireccionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, direccionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DireccionDTO{" +
            "id=" + getId() +
            ", pais='" + getPais() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", localidad='" + getLocalidad() + "'" +
            ", codigoPostal=" + getCodigoPostal() +
            ", calle='" + getCalle() + "'" +
            ", numero='" + getNumero() + "'" +
            ", pacientes=" + getPacientes() +
            ", trabajadors=" + getTrabajadors() +
            "}";
    }
}
