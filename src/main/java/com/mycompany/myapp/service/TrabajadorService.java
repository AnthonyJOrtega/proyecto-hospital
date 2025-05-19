package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Trabajador}.
 */
public interface TrabajadorService {
    /**
     * Save a trabajador.
     *
     * @param trabajadorDTO the entity to save.
     * @return the persisted entity.
     */
    TrabajadorDTO save(TrabajadorDTO trabajadorDTO);

    /**
     * Updates a trabajador.
     *
     * @param trabajadorDTO the entity to update.
     * @return the persisted entity.
     */
    TrabajadorDTO update(TrabajadorDTO trabajadorDTO);

    /**
     * Partially updates a trabajador.
     *
     * @param trabajadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrabajadorDTO> partialUpdate(TrabajadorDTO trabajadorDTO);

    /**
     * Get all the trabajadors with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TrabajadorDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" trabajador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrabajadorDTO> findOne(Long id);

    /**
     * Delete the "id" trabajador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
