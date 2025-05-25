package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.repository.CitaRepository;
import com.mycompany.myapp.repository.InformeRepository;
import com.mycompany.myapp.service.InformeService;
import com.mycompany.myapp.service.dto.InformeDTO;
import com.mycompany.myapp.service.mapper.InformeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Informe}.
 */
@Service
@Transactional
public class InformeServiceImpl implements InformeService {

    private static final Logger LOG = LoggerFactory.getLogger(InformeServiceImpl.class);
    private final InformeRepository informeRepository;

    private final InformeMapper informeMapper;

    private final CitaRepository citaRepository;

    public InformeServiceImpl(InformeRepository informeRepository, InformeMapper informeMapper, CitaRepository citaRepository) {
        this.informeRepository = informeRepository;
        this.informeMapper = informeMapper;
        this.citaRepository = citaRepository;
    }

    @Override
    public InformeDTO save(InformeDTO informeDTO) {
        LOG.debug("Request to save Informe : {}", informeDTO);
        Informe informe = informeMapper.toEntity(informeDTO);
        informe = informeRepository.save(informe);
        if (informeDTO.getCita() != null && informe.getId() != null) {
            Cita cita = citaRepository.findById(informeDTO.getCita().getId()).orElse(null);
            if (cita != null) {
                cita.setInforme(informe); // Esto guarda solo el id en la FK de la tabla cita
                citaRepository.save(cita);
            }
        }
        return informeMapper.toDto(informe);
    }

    @Override
    public InformeDTO update(InformeDTO informeDTO) {
        LOG.debug("Request to update Informe : {}", informeDTO);
        Informe informe = informeMapper.toEntity(informeDTO);
        informe = informeRepository.save(informe);
        return informeMapper.toDto(informe);
    }

    @Override
    public Optional<InformeDTO> partialUpdate(InformeDTO informeDTO) {
        LOG.debug("Request to partially update Informe : {}", informeDTO);

        return informeRepository
            .findById(informeDTO.getId())
            .map(existingInforme -> {
                informeMapper.partialUpdate(existingInforme, informeDTO);

                return existingInforme;
            })
            .map(informeRepository::save)
            .map(informeMapper::toDto);
    }

    public Page<InformeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return informeRepository.findAllWithEagerRelationships(pageable).map(informeMapper::toDto);
    }

    /**
     *  Get all the informes where Cita is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InformeDTO> findAllWhereCitaIsNull() {
        LOG.debug("Request to get all informes where Cita is null");
        return StreamSupport.stream(informeRepository.findAll().spliterator(), false)
            .filter(informe -> informe.getCita() == null)
            .map(informeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InformeDTO> findOne(Long id) {
        LOG.debug("Request to get Informe : {}", id);
        return informeRepository.findOneWithEagerRelationships(id).map(informeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Informe : {}", id);
        informeRepository.deleteById(id);
    }
}
