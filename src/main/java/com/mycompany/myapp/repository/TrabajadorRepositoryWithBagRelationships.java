package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TrabajadorRepositoryWithBagRelationships {
    Optional<Trabajador> fetchBagRelationships(Optional<Trabajador> trabajador);

    List<Trabajador> fetchBagRelationships(List<Trabajador> trabajadors);

    Page<Trabajador> fetchBagRelationships(Page<Trabajador> trabajadors);
}
