package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Paciente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PacienteRepositoryWithBagRelationships {
    Optional<Paciente> fetchBagRelationships(Optional<Paciente> paciente);

    List<Paciente> fetchBagRelationships(List<Paciente> pacientes);

    Page<Paciente> fetchBagRelationships(Page<Paciente> pacientes);
}
