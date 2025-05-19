package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.MedicamentoDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.dto.RecetaDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receta} and its DTO {@link RecetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecetaMapper extends EntityMapper<RecetaDTO, Receta> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "trabajador", source = "trabajador", qualifiedByName = "trabajadorId")
    @Mapping(target = "medicamentos", source = "medicamentos", qualifiedByName = "medicamentoIdSet")
    RecetaDTO toDto(Receta s);

    @Mapping(target = "removeMedicamento", ignore = true)
    Receta toEntity(RecetaDTO recetaDTO);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("medicamentoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicamentoDTO toDtoMedicamentoId(Medicamento medicamento);

    @Named("medicamentoIdSet")
    default Set<MedicamentoDTO> toDtoMedicamentoIdSet(Set<Medicamento> medicamento) {
        return medicamento.stream().map(this::toDtoMedicamentoId).collect(Collectors.toSet());
    }
}
