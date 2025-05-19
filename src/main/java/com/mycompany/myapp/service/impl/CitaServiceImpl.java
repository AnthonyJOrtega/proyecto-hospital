package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.repository.CitaRepository;
import com.mycompany.myapp.service.CitaService;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.mapper.CitaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Cita}.
 */
@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private static final Logger LOG = LoggerFactory.getLogger(CitaServiceImpl.class);

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    public CitaServiceImpl(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    @Override
    public CitaDTO save(CitaDTO citaDTO) {
        LOG.debug("Request to save Cita : {}", citaDTO);
        Cita cita = citaMapper.toEntity(citaDTO);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    public CitaDTO update(CitaDTO citaDTO) {
        LOG.debug("Request to update Cita : {}", citaDTO);
        Cita cita = citaMapper.toEntity(citaDTO);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    public Optional<CitaDTO> partialUpdate(CitaDTO citaDTO) {
        LOG.debug("Request to partially update Cita : {}", citaDTO);

        return citaRepository
            .findById(citaDTO.getId())
            .map(existingCita -> {
                citaMapper.partialUpdate(existingCita, citaDTO);

                return existingCita;
            })
            .map(citaRepository::save)
            .map(citaMapper::toDto);
    }

    public Page<CitaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return citaRepository.findAllWithEagerRelationships(pageable).map(citaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaDTO> findOne(Long id) {
        LOG.debug("Request to get Cita : {}", id);
        return citaRepository.findOneWithEagerRelationships(id).map(citaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cita : {}", id);
        citaRepository.deleteById(id);
    }
}
