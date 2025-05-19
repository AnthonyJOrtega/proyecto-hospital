package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EnfermedadDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Enfermedad}.
 */
public interface EnfermedadService {
    /**
     * Save a enfermedad.
     *
     * @param enfermedadDTO the entity to save.
     * @return the persisted entity.
     */
    EnfermedadDTO save(EnfermedadDTO enfermedadDTO);

    /**
     * Updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update.
     * @return the persisted entity.
     */
    EnfermedadDTO update(EnfermedadDTO enfermedadDTO);

    /**
     * Partially updates a enfermedad.
     *
     * @param enfermedadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO);

    /**
     * Get the "id" enfermedad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnfermedadDTO> findOne(Long id);

    /**
     * Delete the "id" enfermedad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
