package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EspecialidadDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Especialidad}.
 */
public interface EspecialidadService {
    /**
     * Save a especialidad.
     *
     * @param especialidadDTO the entity to save.
     * @return the persisted entity.
     */
    EspecialidadDTO save(EspecialidadDTO especialidadDTO);

    /**
     * Updates a especialidad.
     *
     * @param especialidadDTO the entity to update.
     * @return the persisted entity.
     */
    EspecialidadDTO update(EspecialidadDTO especialidadDTO);

    /**
     * Partially updates a especialidad.
     *
     * @param especialidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EspecialidadDTO> partialUpdate(EspecialidadDTO especialidadDTO);

    /**
     * Get the "id" especialidad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EspecialidadDTO> findOne(Long id);

    /**
     * Delete the "id" especialidad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
