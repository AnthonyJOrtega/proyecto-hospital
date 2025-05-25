package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.InformeDTO;
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
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteNombreApellido")
    @Mapping(target = "trabajador", source = "trabajador", qualifiedByName = "trabajadorNombreApellido")
    @Mapping(target = "medicamentos", source = "medicamentos", qualifiedByName = "medicamentoNombreSet")
    @Mapping(target = "informe", source = "informe", qualifiedByName = "informeId")
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

    // Este mapeo es para obtener el nombre del medicamento
    @Named("medicamentoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MedicamentoDTO toDtoMedicamentoNombre(Medicamento medicamento);

    @Named("medicamentoNombreSet")
    default Set<MedicamentoDTO> toDtoMedicamentoNombreSet(Set<Medicamento> medicamento) {
        return medicamento.stream().map(this::toDtoMedicamentoNombre).collect(Collectors.toSet());
    }

    @Named("informeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InformeDTO toDtoInformeId(Informe informe);

    // Este mapeo es para obtener el nombre del paciente
    @Named("pacienteNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    PacienteDTO toDtoPacienteNombreApellido(Paciente paciente);

    // Este mapeo es para obtener el nombre del trabajador
    @Named("trabajadorNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    TrabajadorDTO toDtoTrabajadorNombreApellido(Trabajador trabajador);
}
