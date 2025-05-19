package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MedicamentoDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Medicamento}.
 */
public interface MedicamentoService {
    /**
     * Save a medicamento.
     *
     * @param medicamentoDTO the entity to save.
     * @return the persisted entity.
     */
    MedicamentoDTO save(MedicamentoDTO medicamentoDTO);

    /**
     * Updates a medicamento.
     *
     * @param medicamentoDTO the entity to update.
     * @return the persisted entity.
     */
    MedicamentoDTO update(MedicamentoDTO medicamentoDTO);

    /**
     * Partially updates a medicamento.
     *
     * @param medicamentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO);

    /**
     * Get the "id" medicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicamentoDTO> findOne(Long id);

    /**
     * Delete the "id" medicamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
