package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Puesto;
import com.mycompany.myapp.domain.enumeration.Turno;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trabajador.
 */
@Entity
@Table(name = "trabajador")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trabajador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "dni")
    private String dni;

    @Enumerated(EnumType.STRING)
    @Column(name = "puesto")
    private Puesto puesto;

    @Column(name = "disponibilidad")
    private Boolean disponibilidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno")
    private Turno turno;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_trabajador__especialidad",
        joinColumns = @JoinColumn(name = "trabajador_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    @JsonIgnoreProperties(value = { "trabajadors" }, allowSetters = true)
    private Set<Especialidad> especialidads = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trabajador")
    @JsonIgnoreProperties(value = { "receta", "paciente", "trabajador", "enfermedads", "cita" }, allowSetters = true)
    private Set<Informe> informes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trabajador")
    @JsonIgnoreProperties(value = { "paciente", "trabajador", "medicamentos", "informe" }, allowSetters = true)
    private Set<Receta> recetas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trabajadors")
    @JsonIgnoreProperties(value = { "informe", "paciente", "trabajadors" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trabajadors")
    @JsonIgnoreProperties(value = { "trabajadors", "citas", "informes", "recetas", "direccions" }, allowSetters = true)
    private Set<Paciente> pacientes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trabajadors")
    @JsonIgnoreProperties(value = { "pacientes", "trabajadors" }, allowSetters = true)
    private Set<Direccion> direccions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trabajador id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return this.idUsuario;
    }

    public Trabajador idUsuario(Long idUsuario) {
        this.setIdUsuario(idUsuario);
        return this;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Trabajador nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Trabajador apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return this.dni;
    }

    public Trabajador dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Puesto getPuesto() {
        return this.puesto;
    }

    public Trabajador puesto(Puesto puesto) {
        this.setPuesto(puesto);
        return this;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public Boolean getDisponibilidad() {
        return this.disponibilidad;
    }

    public Trabajador disponibilidad(Boolean disponibilidad) {
        this.setDisponibilidad(disponibilidad);
        return this;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Turno getTurno() {
        return this.turno;
    }

    public Trabajador turno(Turno turno) {
        this.setTurno(turno);
        return this;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Set<Especialidad> getEspecialidads() {
        return this.especialidads;
    }

    public void setEspecialidads(Set<Especialidad> especialidads) {
        this.especialidads = especialidads;
    }

    public Trabajador especialidads(Set<Especialidad> especialidads) {
        this.setEspecialidads(especialidads);
        return this;
    }

    public Trabajador addEspecialidad(Especialidad especialidad) {
        this.especialidads.add(especialidad);
        return this;
    }

    public Trabajador removeEspecialidad(Especialidad especialidad) {
        this.especialidads.remove(especialidad);
        return this;
    }

    public Set<Informe> getInformes() {
        return this.informes;
    }

    public void setInformes(Set<Informe> informes) {
        if (this.informes != null) {
            this.informes.forEach(i -> i.setTrabajador(null));
        }
        if (informes != null) {
            informes.forEach(i -> i.setTrabajador(this));
        }
        this.informes = informes;
    }

    public Trabajador informes(Set<Informe> informes) {
        this.setInformes(informes);
        return this;
    }

    public Trabajador addInforme(Informe informe) {
        this.informes.add(informe);
        informe.setTrabajador(this);
        return this;
    }

    public Trabajador removeInforme(Informe informe) {
        this.informes.remove(informe);
        informe.setTrabajador(null);
        return this;
    }

    public Set<Receta> getRecetas() {
        return this.recetas;
    }

    public void setRecetas(Set<Receta> recetas) {
        if (this.recetas != null) {
            this.recetas.forEach(i -> i.setTrabajador(null));
        }
        if (recetas != null) {
            recetas.forEach(i -> i.setTrabajador(this));
        }
        this.recetas = recetas;
    }

    public Trabajador recetas(Set<Receta> recetas) {
        this.setRecetas(recetas);
        return this;
    }

    public Trabajador addReceta(Receta receta) {
        this.recetas.add(receta);
        receta.setTrabajador(this);
        return this;
    }

    public Trabajador removeReceta(Receta receta) {
        this.recetas.remove(receta);
        receta.setTrabajador(null);
        return this;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.removeTrabajador(this));
        }
        if (citas != null) {
            citas.forEach(i -> i.addTrabajador(this));
        }
        this.citas = citas;
    }

    public Trabajador citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Trabajador addCita(Cita cita) {
        this.citas.add(cita);
        cita.getTrabajadors().add(this);
        return this;
    }

    public Trabajador removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.getTrabajadors().remove(this);
        return this;
    }

    public Set<Paciente> getPacientes() {
        return this.pacientes;
    }

    public void setPacientes(Set<Paciente> pacientes) {
        if (this.pacientes != null) {
            this.pacientes.forEach(i -> i.removeTrabajador(this));
        }
        if (pacientes != null) {
            pacientes.forEach(i -> i.addTrabajador(this));
        }
        this.pacientes = pacientes;
    }

    public Trabajador pacientes(Set<Paciente> pacientes) {
        this.setPacientes(pacientes);
        return this;
    }

    public Trabajador addPaciente(Paciente paciente) {
        this.pacientes.add(paciente);
        paciente.getTrabajadors().add(this);
        return this;
    }

    public Trabajador removePaciente(Paciente paciente) {
        this.pacientes.remove(paciente);
        paciente.getTrabajadors().remove(this);
        return this;
    }

    public Set<Direccion> getDireccions() {
        return this.direccions;
    }

    public void setDireccions(Set<Direccion> direccions) {
        if (this.direccions != null) {
            this.direccions.forEach(i -> i.removeTrabajador(this));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.addTrabajador(this));
        }
        this.direccions = direccions;
    }

    public Trabajador direccions(Set<Direccion> direccions) {
        this.setDireccions(direccions);
        return this;
    }

    public Trabajador addDireccion(Direccion direccion) {
        this.direccions.add(direccion);
        direccion.getTrabajadors().add(this);
        return this;
    }

    public Trabajador removeDireccion(Direccion direccion) {
        this.direccions.remove(direccion);
        direccion.getTrabajadors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trabajador)) {
            return false;
        }
        return getId() != null && getId().equals(((Trabajador) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trabajador{" +
            "id=" + getId() +
            ", idUsuario=" + getIdUsuario() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni='" + getDni() + "'" +
            ", puesto='" + getPuesto() + "'" +
            ", disponibilidad='" + getDisponibilidad() + "'" +
            ", turno='" + getTurno() + "'" +
            "}";
    }
}
