package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.InformeDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Informe}.
 */
public interface InformeService {
    /**
     * Save a informe.
     *
     * @param informeDTO the entity to save.
     * @return the persisted entity.
     */
    InformeDTO save(InformeDTO informeDTO);

    /**
     * Updates a informe.
     *
     * @param informeDTO the entity to update.
     * @return the persisted entity.
     */
    InformeDTO update(InformeDTO informeDTO);

    /**
     * Partially updates a informe.
     *
     * @param informeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InformeDTO> partialUpdate(InformeDTO informeDTO);

    /**
     * Get all the InformeDTO where Cita is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<InformeDTO> findAllWhereCitaIsNull();

    /**
     * Get all the informes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InformeDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" informe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InformeDTO> findOne(Long id);

    /**
     * Delete the "id" informe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
