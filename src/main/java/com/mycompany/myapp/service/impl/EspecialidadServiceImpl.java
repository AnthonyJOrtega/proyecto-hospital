package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Especialidad;
import com.mycompany.myapp.repository.EspecialidadRepository;
import com.mycompany.myapp.service.EspecialidadService;
import com.mycompany.myapp.service.dto.EspecialidadDTO;
import com.mycompany.myapp.service.mapper.EspecialidadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Especialidad}.
 */
@Service
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private static final Logger LOG = LoggerFactory.getLogger(EspecialidadServiceImpl.class);

    private final EspecialidadRepository especialidadRepository;

    private final EspecialidadMapper especialidadMapper;

    public EspecialidadServiceImpl(EspecialidadRepository especialidadRepository, EspecialidadMapper especialidadMapper) {
        this.especialidadRepository = especialidadRepository;
        this.especialidadMapper = especialidadMapper;
    }

    @Override
    public EspecialidadDTO save(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to save Especialidad : {}", especialidadDTO);
        Especialidad especialidad = especialidadMapper.toEntity(especialidadDTO);
        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(especialidad);
    }

    @Override
    public EspecialidadDTO update(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to update Especialidad : {}", especialidadDTO);
        Especialidad especialidad = especialidadMapper.toEntity(especialidadDTO);
        especialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(especialidad);
    }

    @Override
    public Optional<EspecialidadDTO> partialUpdate(EspecialidadDTO especialidadDTO) {
        LOG.debug("Request to partially update Especialidad : {}", especialidadDTO);

        return especialidadRepository
            .findById(especialidadDTO.getId())
            .map(existingEspecialidad -> {
                especialidadMapper.partialUpdate(existingEspecialidad, especialidadDTO);

                return existingEspecialidad;
            })
            .map(especialidadRepository::save)
            .map(especialidadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EspecialidadDTO> findOne(Long id) {
        LOG.debug("Request to get Especialidad : {}", id);
        return especialidadRepository.findById(id).map(especialidadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Especialidad : {}", id);
        especialidadRepository.deleteById(id);
    }
}
