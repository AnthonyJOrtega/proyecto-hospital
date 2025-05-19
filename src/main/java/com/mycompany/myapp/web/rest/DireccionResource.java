package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.service.DireccionQueryService;
import com.mycompany.myapp.service.DireccionService;
import com.mycompany.myapp.service.criteria.DireccionCriteria;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Direccion}.
 */
@RestController
@RequestMapping("/api/direccions")
public class DireccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionResource.class);

    private static final String ENTITY_NAME = "direccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DireccionService direccionService;

    private final DireccionRepository direccionRepository;

    private final DireccionQueryService direccionQueryService;

    public DireccionResource(
        DireccionService direccionService,
        DireccionRepository direccionRepository,
        DireccionQueryService direccionQueryService
    ) {
        this.direccionService = direccionService;
        this.direccionRepository = direccionRepository;
        this.direccionQueryService = direccionQueryService;
    }

    /**
     * {@code POST  /direccions} : Create a new direccion.
     *
     * @param direccionDTO the direccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direccionDTO, or with status {@code 400 (Bad Request)} if the direccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DireccionDTO> createDireccion(@RequestBody DireccionDTO direccionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Direccion : {}", direccionDTO);
        if (direccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new direccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        direccionDTO = direccionService.save(direccionDTO);
        return ResponseEntity.created(new URI("/api/direccions/" + direccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString()))
            .body(direccionDTO);
    }

    /**
     * {@code PUT  /direccions/:id} : Updates an existing direccion.
     *
     * @param id the id of the direccionDTO to save.
     * @param direccionDTO the direccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccionDTO,
     * or with status {@code 400 (Bad Request)} if the direccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the direccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> updateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DireccionDTO direccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Direccion : {}, {}", id, direccionDTO);
        if (direccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        direccionDTO = direccionService.update(direccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString()))
            .body(direccionDTO);
    }

    /**
     * {@code PATCH  /direccions/:id} : Partial updates given fields of an existing direccion, field will ignore if it is null
     *
     * @param id the id of the direccionDTO to save.
     * @param direccionDTO the direccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccionDTO,
     * or with status {@code 400 (Bad Request)} if the direccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the direccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the direccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DireccionDTO> partialUpdateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DireccionDTO direccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Direccion partially : {}, {}", id, direccionDTO);
        if (direccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DireccionDTO> result = direccionService.partialUpdate(direccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /direccions} : get all the direccions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of direccions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DireccionDTO>> getAllDireccions(
        DireccionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Direccions by criteria: {}", criteria);

        Page<DireccionDTO> page = direccionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /direccions/count} : count all the direccions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDireccions(DireccionCriteria criteria) {
        LOG.debug("REST request to count Direccions by criteria: {}", criteria);
        return ResponseEntity.ok().body(direccionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /direccions/:id} : get the "id" direccion.
     *
     * @param id the id of the direccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> getDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Direccion : {}", id);
        Optional<DireccionDTO> direccionDTO = direccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(direccionDTO);
    }

    /**
     * {@code DELETE  /direccions/:id} : delete the "id" direccion.
     *
     * @param id the id of the direccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Direccion : {}", id);
        direccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
