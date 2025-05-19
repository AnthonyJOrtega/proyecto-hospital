package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.repository.MedicamentoRepository;
import com.mycompany.myapp.service.MedicamentoService;
import com.mycompany.myapp.service.dto.MedicamentoDTO;
import com.mycompany.myapp.service.mapper.MedicamentoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Medicamento}.
 */
@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(MedicamentoServiceImpl.class);

    private final MedicamentoRepository medicamentoRepository;

    private final MedicamentoMapper medicamentoMapper;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository, MedicamentoMapper medicamentoMapper) {
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoMapper = medicamentoMapper;
    }

    @Override
    public MedicamentoDTO save(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to save Medicamento : {}", medicamentoDTO);
        Medicamento medicamento = medicamentoMapper.toEntity(medicamentoDTO);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toDto(medicamento);
    }

    @Override
    public MedicamentoDTO update(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to update Medicamento : {}", medicamentoDTO);
        Medicamento medicamento = medicamentoMapper.toEntity(medicamentoDTO);
        medicamento = medicamentoRepository.save(medicamento);
        return medicamentoMapper.toDto(medicamento);
    }

    @Override
    public Optional<MedicamentoDTO> partialUpdate(MedicamentoDTO medicamentoDTO) {
        LOG.debug("Request to partially update Medicamento : {}", medicamentoDTO);

        return medicamentoRepository
            .findById(medicamentoDTO.getId())
            .map(existingMedicamento -> {
                medicamentoMapper.partialUpdate(existingMedicamento, medicamentoDTO);

                return existingMedicamento;
            })
            .map(medicamentoRepository::save)
            .map(medicamentoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicamentoDTO> findOne(Long id) {
        LOG.debug("Request to get Medicamento : {}", id);
        return medicamentoRepository.findById(id).map(medicamentoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Medicamento : {}", id);
        medicamentoRepository.deleteById(id);
    }
}
