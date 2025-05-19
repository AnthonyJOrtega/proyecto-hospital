package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Medicamento.
 */
@Entity
@Table(name = "medicamento")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "dosis")
    private String dosis;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "medicamentos")
    @JsonIgnoreProperties(value = { "paciente", "trabajador", "medicamentos", "informe" }, allowSetters = true)
    private Set<Receta> recetas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Medicamento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Medicamento nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Medicamento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDosis() {
        return this.dosis;
    }

    public Medicamento dosis(String dosis) {
        this.setDosis(dosis);
        return this;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public Set<Receta> getRecetas() {
        return this.recetas;
    }

    public void setRecetas(Set<Receta> recetas) {
        if (this.recetas != null) {
            this.recetas.forEach(i -> i.removeMedicamento(this));
        }
        if (recetas != null) {
            recetas.forEach(i -> i.addMedicamento(this));
        }
        this.recetas = recetas;
    }

    public Medicamento recetas(Set<Receta> recetas) {
        this.setRecetas(recetas);
        return this;
    }

    public Medicamento addReceta(Receta receta) {
        this.recetas.add(receta);
        receta.getMedicamentos().add(this);
        return this;
    }

    public Medicamento removeReceta(Receta receta) {
        this.recetas.remove(receta);
        receta.getMedicamentos().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicamento)) {
            return false;
        }
        return getId() != null && getId().equals(((Medicamento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicamento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", dosis='" + getDosis() + "'" +
            "}";
    }
}
