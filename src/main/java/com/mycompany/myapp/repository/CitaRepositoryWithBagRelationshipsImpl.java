package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cita;
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
public class CitaRepositoryWithBagRelationshipsImpl implements CitaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CITAS_PARAMETER = "citas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cita> fetchBagRelationships(Optional<Cita> cita) {
        return cita.map(this::fetchTrabajadors);
    }

    @Override
    public Page<Cita> fetchBagRelationships(Page<Cita> citas) {
        return new PageImpl<>(fetchBagRelationships(citas.getContent()), citas.getPageable(), citas.getTotalElements());
    }

    @Override
    public List<Cita> fetchBagRelationships(List<Cita> citas) {
        return Optional.of(citas).map(this::fetchTrabajadors).orElse(Collections.emptyList());
    }

    Cita fetchTrabajadors(Cita result) {
        return entityManager
            .createQuery("select cita from Cita cita left join fetch cita.trabajadors where cita.id = :id", Cita.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Cita> fetchTrabajadors(List<Cita> citas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, citas.size()).forEach(index -> order.put(citas.get(index).getId(), index));
        List<Cita> result = entityManager
            .createQuery("select cita from Cita cita left join fetch cita.trabajadors where cita in :citas", Cita.class)
            .setParameter(CITAS_PARAMETER, citas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
