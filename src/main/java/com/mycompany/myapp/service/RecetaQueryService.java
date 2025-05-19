package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.repository.RecetaRepository;
import com.mycompany.myapp.service.criteria.RecetaCriteria;
import com.mycompany.myapp.service.dto.RecetaDTO;
import com.mycompany.myapp.service.mapper.RecetaMapper;
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
 * Service for executing complex queries for {@link Receta} entities in the database.
 * The main input is a {@link RecetaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecetaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecetaQueryService extends QueryService<Receta> {

    private static final Logger LOG = LoggerFactory.getLogger(RecetaQueryService.class);

    private final RecetaRepository recetaRepository;

    private final RecetaMapper recetaMapper;

    public RecetaQueryService(RecetaRepository recetaRepository, RecetaMapper recetaMapper) {
        this.recetaRepository = recetaRepository;
        this.recetaMapper = recetaMapper;
    }

    /**
     * Return a {@link Page} of {@link RecetaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecetaDTO> findByCriteria(RecetaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Receta> specification = createSpecification(criteria);
        return recetaRepository.fetchBagRelationships(recetaRepository.findAll(specification, page)).map(recetaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecetaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Receta> specification = createSpecification(criteria);
        return recetaRepository.count(specification);
    }

    /**
     * Function to convert {@link RecetaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Receta> createSpecification(RecetaCriteria criteria) {
        Specification<Receta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Receta_.id),
                buildRangeSpecification(criteria.getFechaInicio(), Receta_.fechaInicio),
                buildRangeSpecification(criteria.getFechaFin(), Receta_.fechaFin),
                buildStringSpecification(criteria.getInstrucciones(), Receta_.instrucciones),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Receta_.paciente, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getTrabajadorId(), root -> root.join(Receta_.trabajador, JoinType.LEFT).get(Trabajador_.id)),
                buildSpecification(criteria.getMedicamentoId(), root -> root.join(Receta_.medicamentos, JoinType.LEFT).get(Medicamento_.id)
                ),
                buildSpecification(criteria.getInformeId(), root -> root.join(Receta_.informe, JoinType.LEFT).get(Informe_.id))
            );
        }
        return specification;
    }
}
