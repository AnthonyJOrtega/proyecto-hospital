package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.service.criteria.DireccionCriteria;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.mapper.DireccionMapper;
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
 * Service for executing complex queries for {@link Direccion} entities in the database.
 * The main input is a {@link DireccionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DireccionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DireccionQueryService extends QueryService<Direccion> {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionQueryService.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionQueryService(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    /**
     * Return a {@link Page} of {@link DireccionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DireccionDTO> findByCriteria(DireccionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.fetchBagRelationships(direccionRepository.findAll(specification, page)).map(direccionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DireccionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.count(specification);
    }

    /**
     * Function to convert {@link DireccionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Direccion> createSpecification(DireccionCriteria criteria) {
        Specification<Direccion> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Direccion_.id),
                buildStringSpecification(criteria.getPais(), Direccion_.pais),
                buildStringSpecification(criteria.getCiudad(), Direccion_.ciudad),
                buildStringSpecification(criteria.getLocalidad(), Direccion_.localidad),
                buildRangeSpecification(criteria.getCodigoPostal(), Direccion_.codigoPostal),
                buildStringSpecification(criteria.getCalle(), Direccion_.calle),
                buildStringSpecification(criteria.getNumero(), Direccion_.numero),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Direccion_.pacientes, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getTrabajadorId(), root -> root.join(Direccion_.trabajadors, JoinType.LEFT).get(Trabajador_.id))
            );
        }
        return specification;
    }
}
