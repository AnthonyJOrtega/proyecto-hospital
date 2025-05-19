package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Informe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InformeRepositoryWithBagRelationships {
    Optional<Informe> fetchBagRelationships(Optional<Informe> informe);

    List<Informe> fetchBagRelationships(List<Informe> informes);

    Page<Informe> fetchBagRelationships(Page<Informe> informes);
}
