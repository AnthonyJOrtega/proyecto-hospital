package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.DireccionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Direccion}.
 */
public interface DireccionService {
    /**
     * Save a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    DireccionDTO save(DireccionDTO direccionDTO);

    /**
     * Updates a direccion.
     *
     * @param direccionDTO the entity to update.
     * @return the persisted entity.
     */
    DireccionDTO update(DireccionDTO direccionDTO);

    /**
     * Partially updates a direccion.
     *
     * @param direccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO);

    /**
     * Get all the direccions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DireccionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" direccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DireccionDTO> findOne(Long id);

    /**
     * Delete the "id" direccion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
