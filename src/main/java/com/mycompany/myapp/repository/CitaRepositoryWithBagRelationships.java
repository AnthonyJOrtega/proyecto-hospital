package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cita;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CitaRepositoryWithBagRelationships {
    Optional<Cita> fetchBagRelationships(Optional<Cita> cita);

    List<Cita> fetchBagRelationships(List<Cita> citas);

    Page<Cita> fetchBagRelationships(Page<Cita> citas);
}
