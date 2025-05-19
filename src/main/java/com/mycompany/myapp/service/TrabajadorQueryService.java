package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.service.criteria.TrabajadorCriteria;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import com.mycompany.myapp.service.mapper.TrabajadorMapper;
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
 * Service for executing complex queries for {@link Trabajador} entities in the database.
 * The main input is a {@link TrabajadorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrabajadorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrabajadorQueryService extends QueryService<Trabajador> {

    private static final Logger LOG = LoggerFactory.getLogger(TrabajadorQueryService.class);

    private final TrabajadorRepository trabajadorRepository;

    private final TrabajadorMapper trabajadorMapper;

    public TrabajadorQueryService(TrabajadorRepository trabajadorRepository, TrabajadorMapper trabajadorMapper) {
        this.trabajadorRepository = trabajadorRepository;
        this.trabajadorMapper = trabajadorMapper;
    }

    /**
     * Return a {@link Page} of {@link TrabajadorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrabajadorDTO> findByCriteria(TrabajadorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trabajador> specification = createSpecification(criteria);
        return trabajadorRepository.fetchBagRelationships(trabajadorRepository.findAll(specification, page)).map(trabajadorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrabajadorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Trabajador> specification = createSpecification(criteria);
        return trabajadorRepository.count(specification);
    }

    /**
     * Function to convert {@link TrabajadorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trabajador> createSpecification(TrabajadorCriteria criteria) {
        Specification<Trabajador> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Trabajador_.id),
                buildRangeSpecification(criteria.getIdUsuario(), Trabajador_.idUsuario),
                buildStringSpecification(criteria.getNombre(), Trabajador_.nombre),
                buildStringSpecification(criteria.getApellido(), Trabajador_.apellido),
                buildStringSpecification(criteria.getDni(), Trabajador_.dni),
                buildSpecification(criteria.getPuesto(), Trabajador_.puesto),
                buildSpecification(criteria.getDisponibilidad(), Trabajador_.disponibilidad),
                buildSpecification(criteria.getTurno(), Trabajador_.turno),
                buildSpecification(criteria.getEspecialidadId(), root ->
                    root.join(Trabajador_.especialidads, JoinType.LEFT).get(Especialidad_.id)
                ),
                buildSpecification(criteria.getInformeId(), root -> root.join(Trabajador_.informes, JoinType.LEFT).get(Informe_.id)),
                buildSpecification(criteria.getRecetaId(), root -> root.join(Trabajador_.recetas, JoinType.LEFT).get(Receta_.id)),
                buildSpecification(criteria.getCitaId(), root -> root.join(Trabajador_.citas, JoinType.LEFT).get(Cita_.id)),
                buildSpecification(criteria.getPacienteId(), root -> root.join(Trabajador_.pacientes, JoinType.LEFT).get(Paciente_.id)),
                buildSpecification(criteria.getDireccionId(), root -> root.join(Trabajador_.direccions, JoinType.LEFT).get(Direccion_.id))
            );
        }
        return specification;
    }
}
