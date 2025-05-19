package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Direccion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DireccionRepositoryWithBagRelationships {
    Optional<Direccion> fetchBagRelationships(Optional<Direccion> direccion);

    List<Direccion> fetchBagRelationships(List<Direccion> direccions);

    Page<Direccion> fetchBagRelationships(Page<Direccion> direccions);
}
