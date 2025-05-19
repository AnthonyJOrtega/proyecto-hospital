package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Receta.
 */
@Entity
@Table(name = "receta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "instrucciones")
    private String instrucciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trabajadors", "citas", "informes", "recetas", "direccions" }, allowSetters = true)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "especialidads", "informes", "recetas", "citas", "pacientes", "direccions" }, allowSetters = true)
    private Trabajador trabajador;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_receta__medicamento",
        joinColumns = @JoinColumn(name = "receta_id"),
        inverseJoinColumns = @JoinColumn(name = "medicamento_id")
    )
    @JsonIgnoreProperties(value = { "recetas" }, allowSetters = true)
    private Set<Medicamento> medicamentos = new HashSet<>();

    @JsonIgnoreProperties(value = { "receta", "paciente", "trabajador", "enfermedads", "cita" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "receta")
    private Informe informe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Receta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Receta fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Receta fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getInstrucciones() {
        return this.instrucciones;
    }

    public Receta instrucciones(String instrucciones) {
        this.setInstrucciones(instrucciones);
        return this;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Receta paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Trabajador getTrabajador() {
        return this.trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public Receta trabajador(Trabajador trabajador) {
        this.setTrabajador(trabajador);
        return this;
    }

    public Set<Medicamento> getMedicamentos() {
        return this.medicamentos;
    }

    public void setMedicamentos(Set<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public Receta medicamentos(Set<Medicamento> medicamentos) {
        this.setMedicamentos(medicamentos);
        return this;
    }

    public Receta addMedicamento(Medicamento medicamento) {
        this.medicamentos.add(medicamento);
        return this;
    }

    public Receta removeMedicamento(Medicamento medicamento) {
        this.medicamentos.remove(medicamento);
        return this;
    }

    public Informe getInforme() {
        return this.informe;
    }

    public void setInforme(Informe informe) {
        if (this.informe != null) {
            this.informe.setReceta(null);
        }
        if (informe != null) {
            informe.setReceta(this);
        }
        this.informe = informe;
    }

    public Receta informe(Informe informe) {
        this.setInforme(informe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Receta)) {
            return false;
        }
        return getId() != null && getId().equals(((Receta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Receta{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", instrucciones='" + getInstrucciones() + "'" +
            "}";
    }
}
