package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Paciente.
 */
@Entity
@Table(name = "paciente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "dni")
    private String dni;

    @Column(name = "seguro_medico")
    private String seguroMedico;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "telefono")
    private String telefono;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_paciente__trabajador",
        joinColumns = @JoinColumn(name = "paciente_id"),
        inverseJoinColumns = @JoinColumn(name = "trabajador_id")
    )
    @JsonIgnoreProperties(value = { "especialidads", "informes", "recetas", "citas", "pacientes", "direccions" }, allowSetters = true)
    private Set<Trabajador> trabajadors = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "informe", "paciente", "trabajadors" }, allowSetters = true)
    private Set<Cita> citas = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "receta", "paciente", "trabajador", "enfermedads", "cita" }, allowSetters = true)
    private Set<Informe> informes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paciente")
    @JsonIgnoreProperties(value = { "paciente", "trabajador", "medicamentos", "informe" }, allowSetters = true)
    private Set<Receta> recetas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "pacientes")
    @JsonIgnoreProperties(value = { "pacientes", "trabajadors" }, allowSetters = true)
    private Set<Direccion> direccions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paciente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Paciente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Paciente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return this.dni;
    }

    public Paciente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getSeguroMedico() {
        return this.seguroMedico;
    }

    public Paciente seguroMedico(String seguroMedico) {
        this.setSeguroMedico(seguroMedico);
        return this;
    }

    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Paciente fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Paciente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Set<Trabajador> getTrabajadors() {
        return this.trabajadors;
    }

    public void setTrabajadors(Set<Trabajador> trabajadors) {
        this.trabajadors = trabajadors;
    }

    public Paciente trabajadors(Set<Trabajador> trabajadors) {
        this.setTrabajadors(trabajadors);
        return this;
    }

    public Paciente addTrabajador(Trabajador trabajador) {
        this.trabajadors.add(trabajador);
        return this;
    }

    public Paciente removeTrabajador(Trabajador trabajador) {
        this.trabajadors.remove(trabajador);
        return this;
    }

    public Set<Cita> getCitas() {
        return this.citas;
    }

    public void setCitas(Set<Cita> citas) {
        if (this.citas != null) {
            this.citas.forEach(i -> i.setPaciente(null));
        }
        if (citas != null) {
            citas.forEach(i -> i.setPaciente(this));
        }
        this.citas = citas;
    }

    public Paciente citas(Set<Cita> citas) {
        this.setCitas(citas);
        return this;
    }

    public Paciente addCita(Cita cita) {
        this.citas.add(cita);
        cita.setPaciente(this);
        return this;
    }

    public Paciente removeCita(Cita cita) {
        this.citas.remove(cita);
        cita.setPaciente(null);
        return this;
    }

    public Set<Informe> getInformes() {
        return this.informes;
    }

    public void setInformes(Set<Informe> informes) {
        if (this.informes != null) {
            this.informes.forEach(i -> i.setPaciente(null));
        }
        if (informes != null) {
            informes.forEach(i -> i.setPaciente(this));
        }
        this.informes = informes;
    }

    public Paciente informes(Set<Informe> informes) {
        this.setInformes(informes);
        return this;
    }

    public Paciente addInforme(Informe informe) {
        this.informes.add(informe);
        informe.setPaciente(this);
        return this;
    }

    public Paciente removeInforme(Informe informe) {
        this.informes.remove(informe);
        informe.setPaciente(null);
        return this;
    }

    public Set<Receta> getRecetas() {
        return this.recetas;
    }

    public void setRecetas(Set<Receta> recetas) {
        if (this.recetas != null) {
            this.recetas.forEach(i -> i.setPaciente(null));
        }
        if (recetas != null) {
            recetas.forEach(i -> i.setPaciente(this));
        }
        this.recetas = recetas;
    }

    public Paciente recetas(Set<Receta> recetas) {
        this.setRecetas(recetas);
        return this;
    }

    public Paciente addReceta(Receta receta) {
        this.recetas.add(receta);
        receta.setPaciente(this);
        return this;
    }

    public Paciente removeReceta(Receta receta) {
        this.recetas.remove(receta);
        receta.setPaciente(null);
        return this;
    }

    public Set<Direccion> getDireccions() {
        return this.direccions;
    }

    public void setDireccions(Set<Direccion> direccions) {
        if (this.direccions != null) {
            this.direccions.forEach(i -> i.removePaciente(this));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.addPaciente(this));
        }
        this.direccions = direccions;
    }

    public Paciente direccions(Set<Direccion> direccions) {
        this.setDireccions(direccions);
        return this;
    }

    public Paciente addDireccion(Direccion direccion) {
        this.direccions.add(direccion);
        direccion.getPacientes().add(this);
        return this;
    }

    public Paciente removeDireccion(Direccion direccion) {
        this.direccions.remove(direccion);
        direccion.getPacientes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paciente)) {
            return false;
        }
        return getId() != null && getId().equals(((Paciente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paciente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", dni='" + getDni() + "'" +
            ", seguroMedico='" + getSeguroMedico() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
