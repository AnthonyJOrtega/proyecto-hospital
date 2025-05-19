package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.EstadoCita;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cita.
 */
@Entity
@Table(name = "cita")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cita")
    private EstadoCita estadoCita;

    @Column(name = "observaciones")
    private String observaciones;

    @JsonIgnoreProperties(value = { "receta", "paciente", "trabajador", "enfermedads", "cita" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Informe informe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trabajadors", "citas", "informes", "recetas", "direccions" }, allowSetters = true)
    private Paciente paciente;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_cita__trabajador",
        joinColumns = @JoinColumn(name = "cita_id"),
        inverseJoinColumns = @JoinColumn(name = "trabajador_id")
    )
    @JsonIgnoreProperties(value = { "especialidads", "informes", "recetas", "citas", "pacientes", "direccions" }, allowSetters = true)
    private Set<Trabajador> trabajadors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cita id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Cita fechaCreacion(LocalDate fechaCreacion) {
        this.setFechaCreacion(fechaCreacion);
        return this;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoCita getEstadoCita() {
        return this.estadoCita;
    }

    public Cita estadoCita(EstadoCita estadoCita) {
        this.setEstadoCita(estadoCita);
        return this;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Cita observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Informe getInforme() {
        return this.informe;
    }

    public void setInforme(Informe informe) {
        this.informe = informe;
    }

    public Cita informe(Informe informe) {
        this.setInforme(informe);
        return this;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Cita paciente(Paciente paciente) {
        this.setPaciente(paciente);
        return this;
    }

    public Set<Trabajador> getTrabajadors() {
        return this.trabajadors;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
    }

    public Cita trabajadors(Set<Trabajador> trabajadors) {
        this.setTrabajadors(trabajadors);
        return this;
    }

    public Cita addTrabajador(Trabajador trabajador) {
        this.trabajadors.add(trabajador);
        return this;
    }

    public Cita removeTrabajador(Trabajador trabajador) {
        this.trabajadors.remove(trabajador);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cita)) {
            return false;
        }
        return getId() != null && getId().equals(((Cita) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cita{" +
            "id=" + getId() +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", estadoCita='" + getEstadoCita() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
