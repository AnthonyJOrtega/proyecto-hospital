package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.repository.EnfermedadRepository;
import com.mycompany.myapp.service.EnfermedadService;
import com.mycompany.myapp.service.dto.EnfermedadDTO;
import com.mycompany.myapp.service.mapper.EnfermedadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Enfermedad}.
 */
@Service
@Transactional
public class EnfermedadServiceImpl implements EnfermedadService {

    private static final Logger LOG = LoggerFactory.getLogger(EnfermedadServiceImpl.class);

    private final EnfermedadRepository enfermedadRepository;

    private final EnfermedadMapper enfermedadMapper;

    public EnfermedadServiceImpl(EnfermedadRepository enfermedadRepository, EnfermedadMapper enfermedadMapper) {
        this.enfermedadRepository = enfermedadRepository;
        this.enfermedadMapper = enfermedadMapper;
    }

    @Override
    public EnfermedadDTO save(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to save Enfermedad : {}", enfermedadDTO);
        Enfermedad enfermedad = enfermedadMapper.toEntity(enfermedadDTO);
        enfermedad = enfermedadRepository.save(enfermedad);
        return enfermedadMapper.toDto(enfermedad);
    }

    @Override
    public EnfermedadDTO update(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to update Enfermedad : {}", enfermedadDTO);
        Enfermedad enfermedad = enfermedadMapper.toEntity(enfermedadDTO);
        enfermedad = enfermedadRepository.save(enfermedad);
        return enfermedadMapper.toDto(enfermedad);
    }

    @Override
    public Optional<EnfermedadDTO> partialUpdate(EnfermedadDTO enfermedadDTO) {
        LOG.debug("Request to partially update Enfermedad : {}", enfermedadDTO);

        return enfermedadRepository
            .findById(enfermedadDTO.getId())
            .map(existingEnfermedad -> {
                enfermedadMapper.partialUpdate(existingEnfermedad, enfermedadDTO);

                return existingEnfermedad;
            })
            .map(enfermedadRepository::save)
            .map(enfermedadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnfermedadDTO> findOne(Long id) {
        LOG.debug("Request to get Enfermedad : {}", id);
        return enfermedadRepository.findById(id).map(enfermedadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Enfermedad : {}", id);
        enfermedadRepository.deleteById(id);
    }
}
