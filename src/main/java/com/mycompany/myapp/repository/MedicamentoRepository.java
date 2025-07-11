package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medicamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medicamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long>, JpaSpecificationExecutor<Medicamento> {}
