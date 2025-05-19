package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.repository.InformeRepository;
import com.mycompany.myapp.service.criteria.InformeCriteria;
import com.mycompany.myapp.service.dto.InformeDTO;
import com.mycompany.myapp.service.mapper.InformeMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Informe} entities in the database.
 * The main input is a {@link InformeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InformeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InformeQueryService extends QueryService<Informe> {

    private static final Logger LOG = LoggerFactory.getLogger(InformeQueryService.class);

    private final InformeRepository informeRepository;

    private final InformeMapper informeMapper;

    public InformeQueryService(InformeRepository informeRepository, InformeMapper informeMapper) {
        this.informeRepository = informeRepository;
        this.informeMapper = informeMapper;
    }

    /**
     * Return a {@link Page} of {@link InformeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InformeDTO> findByCriteria(InformeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Informe> specification = createSpecification(criteria);
        return informeRepository.fetchBagRelationships(informeRepository.findAll(specification, page)).map(informeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InformeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Informe> specification = createSpecification(criteria);
        return informeRepository.count(specification);
    }

    /**
     * Function to convert {@link InformeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Informe> createSpecification(InformeCriteria criteria) {
        Specification<Informe> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Informe_.id),
                buildStringSpecification(criteria.getFecha(), Informe_.fecha),
                buildStringSpecification(criteria.getResumen(), Informe_.resumen),
                buildSpecification(criteria.getRecetaId(), root -> root.join(Informe_.receta, JoinType.LEFT).get(Receta_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Informe_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getTrabajadorId(), root -> root.join(Informe_.trabajador, JoinType.LEFT).get(Trabajador_.id)),
                buildSpecification(criteria.getEnfermedadId(), root -> root.join(Informe_.enfermedads, JoinType.LEFT).get(Enfermedad_.id)),
                buildSpecification(criteria.getCitaId(), root -> root.join(Informe_.cita, JoinType.LEFT).get(Cita_.id))
            );
        }
        return specification;
    }
}
