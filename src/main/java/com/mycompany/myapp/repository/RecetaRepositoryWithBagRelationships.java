package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Receta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RecetaRepositoryWithBagRelationships {
    Optional<Receta> fetchBagRelationships(Optional<Receta> receta);

    List<Receta> fetchBagRelationships(List<Receta> recetas);

    Page<Receta> fetchBagRelationships(Page<Receta> recetas);
}
