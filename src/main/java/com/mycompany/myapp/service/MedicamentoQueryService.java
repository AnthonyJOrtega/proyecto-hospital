package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.repository.MedicamentoRepository;
import com.mycompany.myapp.service.criteria.MedicamentoCriteria;
import com.mycompany.myapp.service.dto.MedicamentoDTO;
import com.mycompany.myapp.service.mapper.MedicamentoMapper;
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
 * Service for executing complex queries for {@link Medicamento} entities in the database.
 * The main input is a {@link MedicamentoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MedicamentoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicamentoQueryService extends QueryService<Medicamento> {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentoQueryService.class);

    private final MedicamentoRepository medicamentoRepository;

    private final MedicamentoMapper medicamentoMapper;

    public MedicamentoQueryService(MedicamentoRepository medicamentoRepository, MedicamentoMapper medicamentoMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoMapper = medicamentoMapper;
    }

    /**
     * Return a {@link Page} of {@link MedicamentoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicamentoDTO> findByCriteria(MedicamentoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicamento> specification = createSpecification(criteria);
        return medicamentoRepository.findAll(specification, page).map(medicamentoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicamentoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Medicamento> specification = createSpecification(criteria);
        return medicamentoRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicamentoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medicamento> createSpecification(MedicamentoCriteria criteria) {
        Specification<Medicamento> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Medicamento_.id),
                buildStringSpecification(criteria.getNombre(), Medicamento_.nombre),
                buildStringSpecification(criteria.getDescripcion(), Medicamento_.descripcion),
                buildStringSpecification(criteria.getDosis(), Medicamento_.dosis),
                buildSpecification(criteria.getRecetaId(), root -> root.join(Medicamento_.recetas, JoinType.LEFT).get(Receta_.id))
            );
        }
        return specification;
    }
}
