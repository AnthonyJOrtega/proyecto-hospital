package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.repository.InformeRepository;
import com.mycompany.myapp.repository.RecetaRepository;
import com.mycompany.myapp.service.RecetaService;
import com.mycompany.myapp.service.dto.RecetaDTO;
import com.mycompany.myapp.service.mapper.RecetaMapper;
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
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Receta}.
 */
@Service
@Transactional
public class RecetaServiceImpl implements RecetaService {

    private static final Logger LOG = LoggerFactory.getLogger(RecetaServiceImpl.class);

    private final RecetaRepository recetaRepository;

    private final RecetaMapper recetaMapper;

    private final InformeRepository informeRepository;

    public RecetaServiceImpl(RecetaRepository recetaRepository, RecetaMapper recetaMapper, InformeRepository informeRepository) {
        this.informeRepository = informeRepository;
        this.recetaRepository = recetaRepository;
        this.recetaMapper = recetaMapper;
    }

    @Override
    public RecetaDTO save(RecetaDTO recetaDTO) {
        LOG.debug("Request to save Receta : {}", recetaDTO);
        Receta receta = recetaMapper.toEntity(recetaDTO);
        receta = recetaRepository.save(receta);
        // Asociar la receta al informe si corresponde
        if (recetaDTO.getInforme() != null && receta.getId() != null) {
            Informe informe = informeRepository.findById(recetaDTO.getInforme().getId()).orElse(null);
            if (informe != null) {
                informe.setReceta(receta);
                informeRepository.save(informe);
            }
        }
        return recetaMapper.toDto(receta);
    }

    @Override
    public RecetaDTO update(RecetaDTO recetaDTO) {
        LOG.debug("Request to update Receta : {}", recetaDTO);
        Receta receta = recetaMapper.toEntity(recetaDTO);
        receta = recetaRepository.save(receta);
        // Asociar la receta al informe si corresponde
        if (recetaDTO.getInforme() != null && receta.getId() != null) {
            Informe informe = informeRepository.findById(recetaDTO.getInforme().getId()).orElse(null);
            if (informe != null) {
                informe.setReceta(receta);
                informeRepository.save(informe);
            }
        }
        return recetaMapper.toDto(receta);
    }

    @Override
    public Optional<RecetaDTO> partialUpdate(RecetaDTO recetaDTO) {
        LOG.debug("Request to partially update Receta : {}", recetaDTO);

        return recetaRepository
            .findById(recetaDTO.getId())
            .map(existingReceta -> {
                recetaMapper.partialUpdate(existingReceta, recetaDTO);

                return existingReceta;
            })
            .map(recetaRepository::save)
            .map(recetaMapper::toDto);
    }

    public Page<RecetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return recetaRepository.findAllWithEagerRelationships(pageable).map(recetaMapper::toDto);
    }

    /**
     *  Get all the recetas where Informe is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RecetaDTO> findAllWhereInformeIsNull() {
        LOG.debug("Request to get all recetas where Informe is null");
        return StreamSupport.stream(recetaRepository.findAll().spliterator(), false)
            .filter(receta -> receta.getInforme() == null)
            .map(recetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecetaDTO> findOne(Long id) {
        LOG.debug("Request to get Receta : {}", id);
        return recetaRepository.findOneWithEagerRelationships(id).map(recetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Receta : {}", id);
        recetaRepository.deleteById(id);
    }
}
