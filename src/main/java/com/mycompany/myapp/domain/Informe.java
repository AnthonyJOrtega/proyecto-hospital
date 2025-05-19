package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Informe.
 */
@Entity
@Table(name = "informe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Informe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private String fecha;

    @Column(name = "resumen")
    private String resumen;

    @JsonIgnoreProperties(value = { "paciente", "trabajador", "medicamentos", "informe" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Receta receta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trabajadors", "citas", "informes", "recetas", "direccions" }, allowSetters = true)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "especialidads", "informes", "recetas", "citas", "pacientes", "direccions" }, allowSetters = true)
    private Trabajador trabajador;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_informe__enfermedad",
        joinColumns = @JoinColumn(name = "informe_id"),
        inverseJoinColumns = @JoinColumn(name = "enfermedad_id")
    )
    @JsonIgnoreProperties(value = { "informes" }, allowSetters = true)
    private Set<Enfermedad> enfermedads = new HashSet<>();

    @JsonIgnoreProperties(value = { "informe", "paciente", "trabajadors" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "informe")
    private Cita cita;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Informe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return this.fecha;
    }

    public Informe fecha(String fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getResumen() {
        return this.resumen;
    }

    public Informe resumen(String resumen) {
        this.setResumen(resumen);
        return this;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Receta getReceta() {
        return this.receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public Informe receta(Receta receta) {
        this.setReceta(receta);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Informe paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Trabajador getTrabajador() {
        return this.trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Informe trabajador(Trabajador trabajador) {
        this.setTrabajador(trabajador);
        return this;
    }

    public Set<Enfermedad> getEnfermedads() {
        return this.enfermedads;
    }

    public void setEnfermedads(Set<Enfermedad> enfermedads) {
        this.enfermedads = enfermedads;
    }

    public Informe enfermedads(Set<Enfermedad> enfermedads) {
        this.setEnfermedads(enfermedads);
        return this;
    }

    public Informe addEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.add(enfermedad);
        return this;
    }

    public Informe removeEnfermedad(Enfermedad enfermedad) {
        this.enfermedads.remove(enfermedad);
        return this;
    }

    public Cita getCita() {
        return this.cita;
    }

    public void setCita(Cita cita) {
        if (this.cita != null) {
            this.cita.setInforme(null);
        }
        if (cita != null) {
            cita.setInforme(this);
        }
        this.cita = cita;
    }

    public Informe cita(Cita cita) {
        this.setCita(cita);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Informe)) {
            return false;
        }
        return getId() != null && getId().equals(((Informe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Informe{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", resumen='" + getResumen() + "'" +
            "}";
    }
}
