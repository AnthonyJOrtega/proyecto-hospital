package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Receta;
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
public class RecetaRepositoryWithBagRelationshipsImpl implements RecetaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RECETAS_PARAMETER = "recetas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Receta> fetchBagRelationships(Optional<Receta> receta) {
        return receta.map(this::fetchMedicamentos);
    }

    @Override
    public Page<Receta> fetchBagRelationships(Page<Receta> recetas) {
        return new PageImpl<>(fetchBagRelationships(recetas.getContent()), recetas.getPageable(), recetas.getTotalElements());
    }

    @Override
    public List<Receta> fetchBagRelationships(List<Receta> recetas) {
        return Optional.of(recetas).map(this::fetchMedicamentos).orElse(Collections.emptyList());
    }

    Receta fetchMedicamentos(Receta result) {
        return entityManager
            .createQuery("select receta from Receta receta left join fetch receta.medicamentos where receta.id = :id", Receta.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Receta> fetchMedicamentos(List<Receta> recetas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, recetas.size()).forEach(index -> order.put(recetas.get(index).getId(), index));
        List<Receta> result = entityManager
            .createQuery("select receta from Receta receta left join fetch receta.medicamentos where receta in :recetas", Receta.class)
            .setParameter(RECETAS_PARAMETER, recetas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
