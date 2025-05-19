package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.service.criteria.PacienteCriteria;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.mapper.PacienteMapper;
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
 * Service for executing complex queries for {@link Paciente} entities in the database.
 * The main input is a {@link PacienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PacienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PacienteQueryService extends QueryService<Paciente> {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteQueryService.class);

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteQueryService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    /**
     * Return a {@link Page} of {@link PacienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PacienteDTO> findByCriteria(PacienteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.fetchBagRelationships(pacienteRepository.findAll(specification, page)).map(pacienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PacienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Paciente> specification = createSpecification(criteria);
        return pacienteRepository.count(specification);
    }

    /**
     * Function to convert {@link PacienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Paciente> createSpecification(PacienteCriteria criteria) {
        Specification<Paciente> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Paciente_.id),
                buildStringSpecification(criteria.getNombre(), Paciente_.nombre),
                buildStringSpecification(criteria.getApellido(), Paciente_.apellido),
                buildStringSpecification(criteria.getDni(), Paciente_.dni),
                buildStringSpecification(criteria.getSeguroMedico(), Paciente_.seguroMedico),
                buildRangeSpecification(criteria.getFechaNacimiento(), Paciente_.fechaNacimiento),
                buildStringSpecification(criteria.getTelefono(), Paciente_.telefono),
                buildSpecification(criteria.getTrabajadorId(), root -> root.join(Paciente_.trabajadors, JoinType.LEFT).get(Trabajador_.id)),
                buildSpecification(criteria.getCitaId(), root -> root.join(Paciente_.citas, JoinType.LEFT).get(Cita_.id)),
                buildSpecification(criteria.getInformeId(), root -> root.join(Paciente_.informes, JoinType.LEFT).get(Informe_.id)),
                buildSpecification(criteria.getRecetaId(), root -> root.join(Paciente_.recetas, JoinType.LEFT).get(Receta_.id)),
                buildSpecification(criteria.getDireccionId(), root -> root.join(Paciente_.direccions, JoinType.LEFT).get(Direccion_.id))
            );
        }
        return specification;
    }
}
