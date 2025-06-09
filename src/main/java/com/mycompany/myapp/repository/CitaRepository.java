package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cita;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cita entity.
 *
 * When extending this class, extend CitaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CitaRepository extends CitaRepositoryWithBagRelationships, JpaRepository<Cita, Long>, JpaSpecificationExecutor<Cita> {
    default Optional<Cita> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Cita> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Cita> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    boolean existsByTrabajadorsIdAndFechaCreacionAndHoraCreacionBetween(
        Long trabajadorId,
        LocalDate fechaCreacion,
        LocalTime horaInicio,
        LocalTime horaFin
    );
}
