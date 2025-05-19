package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Especialidad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Especialidad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long>, JpaSpecificationExecutor<Especialidad> {}
