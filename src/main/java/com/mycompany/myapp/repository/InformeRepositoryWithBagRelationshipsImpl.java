package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Informe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class InformeRepositoryWithBagRelationshipsImpl implements InformeRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String INFORMES_PARAMETER = "informes";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Informe> fetchBagRelationships(Optional<Informe> informe) {
        return informe.map(this::fetchEnfermedads);
    }

    @Override
    public Page<Informe> fetchBagRelationships(Page<Informe> informes) {
        return new PageImpl<>(fetchBagRelationships(informes.getContent()), informes.getPageable(), informes.getTotalElements());
    }

    @Override
    public List<Informe> fetchBagRelationships(List<Informe> informes) {
        return Optional.of(informes).map(this::fetchEnfermedads).orElse(Collections.emptyList());
    }

    Informe fetchEnfermedads(Informe result) {
        return entityManager
            .createQuery("select informe from Informe informe left join fetch informe.enfermedads where informe.id = :id", Informe.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Informe> fetchEnfermedads(List<Informe> informes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, informes.size()).forEach(index -> order.put(informes.get(index).getId(), index));
        List<Informe> result = entityManager
            .createQuery(
                "select informe from Informe informe left join fetch informe.enfermedads where informe in :informes",
                Informe.class
            )
            .setParameter(INFORMES_PARAMETER, informes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
