package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.InformeRepository;
import com.mycompany.myapp.service.InformeQueryService;
import com.mycompany.myapp.service.InformeService;
import com.mycompany.myapp.service.criteria.InformeCriteria;
import com.mycompany.myapp.service.dto.InformeDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Informe}.
 */
@RestController
@RequestMapping("/api/informes")
public class InformeResource {

    private static final Logger LOG = LoggerFactory.getLogger(InformeResource.class);

    private static final String ENTITY_NAME = "informe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InformeService informeService;

    private final InformeRepository informeRepository;

    private final InformeQueryService informeQueryService;

    public InformeResource(InformeService informeService, InformeRepository informeRepository, InformeQueryService informeQueryService) {
        this.informeService = informeService;
        this.informeRepository = informeRepository;
        this.informeQueryService = informeQueryService;
    }

    /**
     * {@code POST  /informes} : Create a new informe.
     *
     * @param informeDTO the informeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new informeDTO, or with status {@code 400 (Bad Request)} if the informe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InformeDTO> createInforme(@RequestBody InformeDTO informeDTO) throws URISyntaxException {
        LOG.debug("REST request to save Informe : {}", informeDTO);
        if (informeDTO.getId() != null) {
            throw new BadRequestAlertException("A new informe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        informeDTO = informeService.save(informeDTO);
        return ResponseEntity.created(new URI("/api/informes/" + informeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, informeDTO.getId().toString()))
            .body(informeDTO);
    }

    /**
     * {@code PUT  /informes/:id} : Updates an existing informe.
     *
     * @param id the id of the informeDTO to save.
     * @param informeDTO the informeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informeDTO,
     * or with status {@code 400 (Bad Request)} if the informeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the informeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InformeDTO> updateInforme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InformeDTO informeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Informe : {}, {}", id, informeDTO);
        if (informeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        informeDTO = informeService.update(informeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informeDTO.getId().toString()))
            .body(informeDTO);
    }

    /**
     * {@code PATCH  /informes/:id} : Partial updates given fields of an existing informe, field will ignore if it is null
     *
     * @param id the id of the informeDTO to save.
     * @param informeDTO the informeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated informeDTO,
     * or with status {@code 400 (Bad Request)} if the informeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the informeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the informeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InformeDTO> partialUpdateInforme(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InformeDTO informeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Informe partially : {}, {}", id, informeDTO);
        if (informeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, informeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!informeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InformeDTO> result = informeService.partialUpdate(informeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, informeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /informes} : get all the informes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of informes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InformeDTO>> getAllInformes(
        InformeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Informes by criteria: {}", criteria);

        Page<InformeDTO> page = informeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /informes/count} : count all the informes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInformes(InformeCriteria criteria) {
        LOG.debug("REST request to count Informes by criteria: {}", criteria);
        return ResponseEntity.ok().body(informeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /informes/:id} : get the "id" informe.
     *
     * @param id the id of the informeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the informeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InformeDTO> getInforme(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Informe : {}", id);
        Optional<InformeDTO> informeDTO = informeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(informeDTO);
    }

    /**
     * {@code DELETE  /informes/:id} : delete the "id" informe.
     *
     * @param id the id of the informeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInforme(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Informe : {}", id);
        informeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
