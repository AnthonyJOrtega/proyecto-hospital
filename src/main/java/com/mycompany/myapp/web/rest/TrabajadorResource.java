package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.service.TrabajadorQueryService;
import com.mycompany.myapp.service.TrabajadorService;
import com.mycompany.myapp.service.criteria.TrabajadorCriteria;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Trabajador}.
 */
@RestController
@RequestMapping("/api/trabajadors")
public class TrabajadorResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrabajadorResource.class);

    private static final String ENTITY_NAME = "trabajador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrabajadorService trabajadorService;

    private final TrabajadorRepository trabajadorRepository;

    private final TrabajadorQueryService trabajadorQueryService;

    public TrabajadorResource(
        TrabajadorService trabajadorService,
        TrabajadorRepository trabajadorRepository,
        TrabajadorQueryService trabajadorQueryService
    ) {
        this.trabajadorService = trabajadorService;
        this.trabajadorRepository = trabajadorRepository;
        this.trabajadorQueryService = trabajadorQueryService;
    }

    /**
     * {@code POST  /trabajadors} : Create a new trabajador.
     *
     * @param trabajadorDTO the trabajadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trabajadorDTO, or with status {@code 400 (Bad Request)} if the trabajador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrabajadorDTO> createTrabajador(@RequestBody TrabajadorDTO trabajadorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Trabajador : {}", trabajadorDTO);
        if (trabajadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new trabajador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trabajadorDTO = trabajadorService.save(trabajadorDTO);
        return ResponseEntity.created(new URI("/api/trabajadors/" + trabajadorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trabajadorDTO.getId().toString()))
            .body(trabajadorDTO);
    }

    /**
     * {@code PUT  /trabajadors/:id} : Updates an existing trabajador.
     *
     * @param id the id of the trabajadorDTO to save.
     * @param trabajadorDTO the trabajadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trabajadorDTO,
     * or with status {@code 400 (Bad Request)} if the trabajadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trabajadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrabajadorDTO> updateTrabajador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrabajadorDTO trabajadorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Trabajador : {}, {}", id, trabajadorDTO);
        if (trabajadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trabajadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trabajadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trabajadorDTO = trabajadorService.update(trabajadorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trabajadorDTO.getId().toString()))
            .body(trabajadorDTO);
    }

    /**
     * {@code PATCH  /trabajadors/:id} : Partial updates given fields of an existing trabajador, field will ignore if it is null
     *
     * @param id the id of the trabajadorDTO to save.
     * @param trabajadorDTO the trabajadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trabajadorDTO,
     * or with status {@code 400 (Bad Request)} if the trabajadorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trabajadorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trabajadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrabajadorDTO> partialUpdateTrabajador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrabajadorDTO trabajadorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Trabajador partially : {}, {}", id, trabajadorDTO);
        if (trabajadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trabajadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trabajadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrabajadorDTO> result = trabajadorService.partialUpdate(trabajadorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trabajadorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trabajadors} : get all the trabajadors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trabajadors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrabajadorDTO>> getAllTrabajadors(
        TrabajadorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Trabajadors by criteria: {}", criteria);

        Page<TrabajadorDTO> page = trabajadorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trabajadors/count} : count all the trabajadors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrabajadors(TrabajadorCriteria criteria) {
        LOG.debug("REST request to count Trabajadors by criteria: {}", criteria);
        return ResponseEntity.ok().body(trabajadorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trabajadors/:id} : get the "id" trabajador.
     *
     * @param id the id of the trabajadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trabajadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorDTO> getTrabajador(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Trabajador : {}", id);
        Optional<TrabajadorDTO> trabajadorDTO = trabajadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trabajadorDTO);
    }

    /**
     * {@code DELETE  /trabajadors/:id} : delete the "id" trabajador.
     *
     * @param id the id of the trabajadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrabajador(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Trabajador : {}", id);
        trabajadorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
