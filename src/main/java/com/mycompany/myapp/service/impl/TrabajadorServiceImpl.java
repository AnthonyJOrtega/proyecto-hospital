package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.repository.DireccionRepository;
import com.mycompany.myapp.repository.TrabajadorRepository;
import com.mycompany.myapp.service.TrabajadorService;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import com.mycompany.myapp.service.mapper.TrabajadorMapper;
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
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Trabajador}.
 */
@Service
@Transactional
public class TrabajadorServiceImpl implements TrabajadorService {

    private static final Logger LOG = LoggerFactory.getLogger(TrabajadorServiceImpl.class);
    private final TrabajadorRepository trabajadorRepository;

    private final TrabajadorMapper trabajadorMapper;

    private final DireccionRepository direccionRepository;

    public TrabajadorServiceImpl(
        TrabajadorRepository trabajadorRepository,
        TrabajadorMapper trabajadorMapper,
        DireccionRepository direccionRepository
    ) {
        this.trabajadorRepository = trabajadorRepository;
        this.trabajadorMapper = trabajadorMapper;
        this.direccionRepository = direccionRepository;
    }

    @Override
    public TrabajadorDTO save(TrabajadorDTO trabajadorDTO) {
        LOG.debug("Request to save Trabajador : {}", trabajadorDTO);
        Trabajador trabajador = trabajadorMapper.toEntity(trabajadorDTO);

        // Asignar direcciones seleccionadas
        Set<Direccion> direccions = new HashSet<>();
        if (trabajadorDTO.getDireccions() != null) {
            for (DireccionDTO direccionDTO : trabajadorDTO.getDireccions()) {
                direccionRepository.findById(direccionDTO.getId()).ifPresent(direccions::add);
            }
        }
        trabajador.setDireccions(direccions);

        trabajador = trabajadorRepository.save(trabajador);
        return trabajadorMapper.toDto(trabajador);
    }

    @Override
    public TrabajadorDTO update(TrabajadorDTO trabajadorDTO) {
        LOG.debug("Request to update Trabajador : {}", trabajadorDTO);
        Trabajador trabajador = trabajadorMapper.toEntity(trabajadorDTO);
        // Asignar direcciones seleccionadas
        Set<Direccion> direccions = new HashSet<>();
        if (trabajadorDTO.getDireccions() != null) {
            for (DireccionDTO direccionDTO : trabajadorDTO.getDireccions()) {
                direccionRepository.findById(direccionDTO.getId()).ifPresent(direccions::add);
            }
        }
        trabajador.setDireccions(direccions);

        trabajador = trabajadorRepository.save(trabajador);
        return trabajadorMapper.toDto(trabajador);
    }

    @Override
    public Optional<TrabajadorDTO> partialUpdate(TrabajadorDTO trabajadorDTO) {
        LOG.debug("Request to partially update Trabajador : {}", trabajadorDTO);

        return trabajadorRepository
            .findById(trabajadorDTO.getId())
            .map(existingTrabajador -> {
                trabajadorMapper.partialUpdate(existingTrabajador, trabajadorDTO);

                return existingTrabajador;
            })
            .map(trabajadorRepository::save)
            .map(trabajadorMapper::toDto);
    }

    public Page<TrabajadorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trabajadorRepository.findAllWithEagerRelationships(pageable).map(trabajadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrabajadorDTO> findOne(Long id) {
        LOG.debug("Request to get Trabajador : {}", id);
        return trabajadorRepository.findOneWithEagerRelationships(id).map(trabajadorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Trabajador : {}", id);
        trabajadorRepository.deleteById(id);
    }
}
