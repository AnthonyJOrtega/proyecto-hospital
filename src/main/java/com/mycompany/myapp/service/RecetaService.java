package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.RecetaDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Receta}.
 */
public interface RecetaService {
    /**
     * Save a receta.
     *
     * @param recetaDTO the entity to save.
     * @return the persisted entity.
     */
    RecetaDTO save(RecetaDTO recetaDTO);

    /**
     * Updates a receta.
     *
     * @param recetaDTO the entity to update.
     * @return the persisted entity.
     */
    RecetaDTO update(RecetaDTO recetaDTO);

    /**
     * Partially updates a receta.
     *
     * @param recetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecetaDTO> partialUpdate(RecetaDTO recetaDTO);

    /**
     * Get all the RecetaDTO where Informe is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<RecetaDTO> findAllWhereInformeIsNull();

    /**
     * Get all the recetas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecetaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" receta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecetaDTO> findOne(Long id);

    /**
     * Delete the "id" receta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
