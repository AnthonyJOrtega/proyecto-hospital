package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.repository.PacienteRepository;
import com.mycompany.myapp.service.PacienteService;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.mapper.PacienteMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Paciente}.
 */
@Service
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private static final Logger LOG = LoggerFactory.getLogger(PacienteServiceImpl.class);
    private final PacienteRepository pacienteRepository;

    private final DireccionRepository direccionRepository;

    private final PacienteMapper pacienteMapper;

    public PacienteServiceImpl(
        PacienteRepository pacienteRepository,
        DireccionRepository direccionRepository,
        PacienteMapper pacienteMapper
    ) {
        this.pacienteRepository = pacienteRepository;
        this.direccionRepository = direccionRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public PacienteDTO save(PacienteDTO pacienteDTO) {
        LOG.debug("Request to save Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);

        // Asignar direcciones seleccionadas
        Set<Direccion> direccions = new HashSet<>();
        if (pacienteDTO.getDireccions() != null) {
            for (DireccionDTO direccionDTO : pacienteDTO.getDireccions()) {
                direccionRepository.findById(direccionDTO.getId()).ifPresent(direccions::add);
            }
        }
        paciente.setDireccions(direccions);

        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    @Override
    public PacienteDTO update(PacienteDTO pacienteDTO) {
        LOG.debug("Request to update Paciente : {}", pacienteDTO);
        Paciente paciente = pacienteMapper.toEntity(pacienteDTO);

        // Asignar direcciones seleccionadas
        Set<Direccion> direccions = new HashSet<>();
        if (pacienteDTO.getDireccions() != null) {
            for (DireccionDTO direccionDTO : pacienteDTO.getDireccions()) {
                direccionRepository.findById(direccionDTO.getId()).ifPresent(direccions::add);
            }
        }
        paciente.setDireccions(direccions);

        paciente = pacienteRepository.save(paciente);
        return pacienteMapper.toDto(paciente);
    }

    @Override
    public Optional<PacienteDTO> partialUpdate(PacienteDTO pacienteDTO) {
        LOG.debug("Request to partially update Paciente : {}", pacienteDTO);

        return pacienteRepository
            .findById(pacienteDTO.getId())
            .map(existingPaciente -> {
                pacienteMapper.partialUpdate(existingPaciente, pacienteDTO);

                return existingPaciente;
            })
            .map(pacienteRepository::save)
            .map(pacienteMapper::toDto);
    }

    public Page<PacienteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pacienteRepository.findAllWithEagerRelationships(pageable).map(pacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findOne(Long id) {
        LOG.debug("Request to get Paciente : {}", id);
        return pacienteRepository.findOneWithEagerRelationships(id).map(pacienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Paciente : {}", id);
        pacienteRepository.deleteById(id);
    }
}
