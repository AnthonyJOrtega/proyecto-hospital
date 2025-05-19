package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Enfermedad;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Enfermedad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnfermedadRepository extends JpaRepository<Enfermedad, Long>, JpaSpecificationExecutor<Enfermedad> {}
