package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Especialidad.
 */
@Entity
@Table(name = "especialidad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Especialidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "especialidads")
    @JsonIgnoreProperties(value = { "especialidads", "informes", "recetas", "citas", "pacientes", "direccions" }, allowSetters = true)
    private Set<Trabajador> trabajadors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Especialidad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Especialidad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Especialidad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Trabajador> getTrabajadors() {
        return this.trabajadors;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        if (this.trabajadors != null) {
            this.trabajadors.forEach(i -> i.removeEspecialidad(this));
        }
        if (trabajadors != null) {
            trabajadors.forEach(i -> i.addEspecialidad(this));
        }
        this.trabajadors = trabajadors;
    }

    public Especialidad trabajadors(Set<Trabajador> trabajadors) {
        this.setTrabajadors(trabajadors);
        return this;
    }

    public Especialidad addTrabajador(Trabajador trabajador) {
        this.trabajadors.add(trabajador);
        trabajador.getEspecialidads().add(this);
        return this;
    }

    public Especialidad removeTrabajador(Trabajador trabajador) {
        this.trabajadors.remove(trabajador);
        trabajador.getEspecialidads().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Especialidad)) {
            return false;
        }
        return getId() != null && getId().equals(((Especialidad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Especialidad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
