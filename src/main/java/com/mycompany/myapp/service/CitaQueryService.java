package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.repository.CitaRepository;
import com.mycompany.myapp.service.criteria.CitaCriteria;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.mapper.CitaMapper;
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
 * Service for executing complex queries for {@link Cita} entities in the database.
 * The main input is a {@link CitaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CitaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CitaQueryService extends QueryService<Cita> {

    private static final Logger LOG = LoggerFactory.getLogger(CitaQueryService.class);

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    public CitaQueryService(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    /**
     * Return a {@link Page} of {@link CitaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CitaDTO> findByCriteria(CitaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cita> specification = createSpecification(criteria);
        return citaRepository.fetchBagRelationships(citaRepository.findAll(specification, page)).map(citaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CitaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cita> specification = createSpecification(criteria);
        return citaRepository.count(specification);
    }

    /**
     * Function to convert {@link CitaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cita> createSpecification(CitaCriteria criteria) {
        Specification<Cita> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cita_.id),
                buildRangeSpecification(criteria.getFechaCreacion(), Cita_.fechaCreacion),
                buildSpecification(criteria.getEstadoCita(), Cita_.estadoCita),
                buildStringSpecification(criteria.getObservaciones(), Cita_.observaciones),
                buildSpecification(criteria.getInformeId(), root -> root.join(Cita_.informe, JoinType.LEFT).get(Informe_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Cita_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getTrabajadorId(), root -> root.join(Cita_.trabajadors, JoinType.LEFT).get(Trabajador_.id))
            );
        }
        return specification;
    }
}
