package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Trabajador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trabajador entity.
 *
 * When extending this class, extend TrabajadorRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TrabajadorRepository
    extends TrabajadorRepositoryWithBagRelationships, JpaRepository<Trabajador, Long>, JpaSpecificationExecutor<Trabajador> {
    default Optional<Trabajador> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Trabajador> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Trabajador> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
