package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Direccion.
 */
@Entity
@Table(name = "direccion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pais")
    private String pais;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "codigo_postal")
    private Integer codigoPostal;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private String numero;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "direccions")
    @JsonIgnoreProperties(value = { "direccions", "trabajadors" }, allowSetters = true)
    private Set<Paciente> pacientes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "direccions")
    @JsonIgnoreProperties(value = { "direccions", "pacientes" }, allowSetters = true)
    private Set<Trabajador> trabajadors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Direccion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPais() {
        return this.pais;
    }

    public Direccion pais(String pais) {
        this.setPais(pais);
        return this;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public Direccion ciudad(String ciudad) {
        this.setCiudad(ciudad);
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLocalidad() {
        return this.localidad;
    }

    public Direccion localidad(String localidad) {
        this.setLocalidad(localidad);
        return this;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Integer getCodigoPostal() {
        return this.codigoPostal;
    }

    public Direccion codigoPostal(Integer codigoPostal) {
        this.setCodigoPostal(codigoPostal);
        return this;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() {
        return this.calle;
    }

    public Direccion calle(String calle) {
        this.setCalle(calle);
        return this;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return this.numero;
    }

    public Direccion numero(String numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Set<Paciente> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public Direccion pacientes(Set<Paciente> pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public Direccion addPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
        return this;
    }

    public Direccion removePaciente(Paciente paciente) {
        this.pacientes.remove(paciente);
        return this;
    }

    public Set<Trabajador> getTrabajadors() {
        return this.trabajadors;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
    }

    public Direccion trabajadors(Set<Trabajador> trabajadors) {
        this.setTrabajadors(trabajadors);
        return this;
    }

    public Direccion addTrabajador(Trabajador trabajador) {
        this.trabajadors.add(trabajador);
        return this;
    }

    public Direccion removeTrabajador(Trabajador trabajador) {
        this.trabajadors.remove(trabajador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Direccion)) {
            return false;
        }
        return getId() != null && getId().equals(((Direccion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Direccion{" +
            "id=" + getId() +
            ", pais='" + getPais() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", localidad='" + getLocalidad() + "'" +
            ", codigoPostal=" + getCodigoPostal() +
            ", calle='" + getCalle() + "'" +
            ", numero='" + getNumero() + "'" +
            "}";
    }
}
