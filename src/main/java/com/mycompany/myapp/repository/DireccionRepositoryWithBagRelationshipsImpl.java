package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Direccion;
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
public class DireccionRepositoryWithBagRelationshipsImpl implements DireccionRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DIRECCIONS_PARAMETER = "direccions";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Direccion> fetchBagRelationships(Optional<Direccion> direccion) {
        return direccion.map(this::fetchPacientes).map(this::fetchTrabajadors);
    }

    @Override
    public Page<Direccion> fetchBagRelationships(Page<Direccion> direccions) {
        return new PageImpl<>(fetchBagRelationships(direccions.getContent()), direccions.getPageable(), direccions.getTotalElements());
    }

    @Override
    public List<Direccion> fetchBagRelationships(List<Direccion> direccions) {
        return Optional.of(direccions).map(this::fetchPacientes).map(this::fetchTrabajadors).orElse(Collections.emptyList());
    }

    Direccion fetchPacientes(Direccion result) {
        return entityManager
            .createQuery(
                "select direccion from Direccion direccion left join fetch direccion.pacientes where direccion.id = :id",
                Direccion.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Direccion> fetchPacientes(List<Direccion> direccions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, direccions.size()).forEach(index -> order.put(direccions.get(index).getId(), index));
        List<Direccion> result = entityManager
            .createQuery(
                "select direccion from Direccion direccion left join fetch direccion.pacientes where direccion in :direccions",
                Direccion.class
            )
            .setParameter(DIRECCIONS_PARAMETER, direccions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Direccion fetchTrabajadors(Direccion result) {
        return entityManager
            .createQuery(
                "select direccion from Direccion direccion left join fetch direccion.trabajadors where direccion.id = :id",
                Direccion.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Direccion> fetchTrabajadors(List<Direccion> direccions) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, direccions.size()).forEach(index -> order.put(direccions.get(index).getId(), index));
        List<Direccion> result = entityManager
            .createQuery(
                "select direccion from Direccion direccion left join fetch direccion.trabajadors where direccion in :direccions",
                Direccion.class
            )
            .setParameter(DIRECCIONS_PARAMETER, direccions)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
