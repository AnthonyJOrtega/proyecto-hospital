package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Paciente;
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
public class PacienteRepositoryWithBagRelationshipsImpl implements PacienteRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PACIENTES_PARAMETER = "pacientes";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Paciente> fetchBagRelationships(Optional<Paciente> paciente) {
        return paciente.map(this::fetchTrabajadors);
    }

    @Override
    public Page<Paciente> fetchBagRelationships(Page<Paciente> pacientes) {
        return new PageImpl<>(fetchBagRelationships(pacientes.getContent()), pacientes.getPageable(), pacientes.getTotalElements());
    }

    @Override
    public List<Paciente> fetchBagRelationships(List<Paciente> pacientes) {
        return Optional.of(pacientes).map(this::fetchTrabajadors).orElse(Collections.emptyList());
    }

    Paciente fetchTrabajadors(Paciente result) {
        return entityManager
            .createQuery(
                "select paciente from Paciente paciente left join fetch paciente.trabajadors where paciente.id = :id",
                Paciente.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Paciente> fetchTrabajadors(List<Paciente> pacientes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, pacientes.size()).forEach(index -> order.put(pacientes.get(index).getId(), index));
        List<Paciente> result = entityManager
            .createQuery(
                "select paciente from Paciente paciente left join fetch paciente.trabajadors where paciente in :pacientes",
                Paciente.class
            )
            .setParameter(PACIENTES_PARAMETER, pacientes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
