package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajador;
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
public class TrabajadorRepositoryWithBagRelationshipsImpl implements TrabajadorRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRABAJADORS_PARAMETER = "trabajadors";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Trabajador> fetchBagRelationships(Optional<Trabajador> trabajador) {
        return trabajador.map(this::fetchEspecialidads);
    }

    @Override
    public Page<Trabajador> fetchBagRelationships(Page<Trabajador> trabajadors) {
        return new PageImpl<>(fetchBagRelationships(trabajadors.getContent()), trabajadors.getPageable(), trabajadors.getTotalElements());
    }

    @Override
    public List<Trabajador> fetchBagRelationships(List<Trabajador> trabajadors) {
        return Optional.of(trabajadors).map(this::fetchEspecialidads).orElse(Collections.emptyList());
    }

    Trabajador fetchEspecialidads(Trabajador result) {
        return entityManager
            .createQuery(
                "select trabajador from Trabajador trabajador left join fetch trabajador.especialidads where trabajador.id = :id",
                Trabajador.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Trabajador> fetchEspecialidads(List<Trabajador> trabajadors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trabajadors.size()).forEach(index -> order.put(trabajadors.get(index).getId(), index));
        List<Trabajador> result = entityManager
            .createQuery(
                "select trabajador from Trabajador trabajador left join fetch trabajador.especialidads where trabajador in :trabajadors",
                Trabajador.class
            )
            .setParameter(TRABAJADORS_PARAMETER, trabajadors)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
