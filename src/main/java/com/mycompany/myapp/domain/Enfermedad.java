package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Enfermedad.
 */
@Entity
@Table(name = "enfermedad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enfermedad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "enfermedads")
    @JsonIgnoreProperties(value = { "receta", "paciente", "trabajador", "enfermedads", "cita" }, allowSetters = true)
    private Set<Informe> informes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enfermedad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Enfermedad nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Enfermedad descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Informe> getInformes() {
        return this.informes;
    }

    public void setInformes(Set<Informe> informes) {
        if (this.informes != null) {
            this.informes.forEach(i -> i.removeEnfermedad(this));
        }
        if (informes != null) {
            informes.forEach(i -> i.addEnfermedad(this));
        }
        this.informes = informes;
    }

    public Enfermedad informes(Set<Informe> informes) {
        this.setInformes(informes);
        return this;
    }

    public Enfermedad addInforme(Informe informe) {
        this.informes.add(informe);
        informe.getEnfermedads().add(this);
        return this;
    }

    public Enfermedad removeInforme(Informe informe) {
        this.informes.remove(informe);
        informe.getEnfermedads().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enfermedad)) {
            return false;
        }
        return getId() != null && getId().equals(((Enfermedad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enfermedad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
