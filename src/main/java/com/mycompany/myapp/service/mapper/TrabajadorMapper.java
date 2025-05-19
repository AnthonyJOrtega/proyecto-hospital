package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Especialidad;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.dto.EspecialidadDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Trabajador} and its DTO {@link TrabajadorDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrabajadorMapper extends EntityMapper<TrabajadorDTO, Trabajador> {
    @Mapping(target = "especialidads", source = "especialidads", qualifiedByName = "especialidadIdSet")
    @Mapping(target = "citas", source = "citas", qualifiedByName = "citaIdSet")
    @Mapping(target = "pacientes", source = "pacientes", qualifiedByName = "pacienteIdSet")
    @Mapping(target = "direccions", source = "direccions", qualifiedByName = "direccionIdSet")
    TrabajadorDTO toDto(Trabajador s);

    @Mapping(target = "removeEspecialidad", ignore = true)
    @Mapping(target = "citas", ignore = true)
    @Mapping(target = "removeCita", ignore = true)
    @Mapping(target = "pacientes", ignore = true)
    @Mapping(target = "removePaciente", ignore = true)
    @Mapping(target = "direccions", ignore = true)
    @Mapping(target = "removeDireccion", ignore = true)
    Trabajador toEntity(TrabajadorDTO trabajadorDTO);

    @Named("especialidadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EspecialidadDTO toDtoEspecialidadId(Especialidad especialidad);

    @Named("especialidadIdSet")
    default Set<EspecialidadDTO> toDtoEspecialidadIdSet(Set<Especialidad> especialidad) {
        return especialidad.stream().map(this::toDtoEspecialidadId).collect(Collectors.toSet());
    }

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);

    @Named("citaIdSet")
    default Set<CitaDTO> toDtoCitaIdSet(Set<Cita> cita) {
        return cita.stream().map(this::toDtoCitaId).collect(Collectors.toSet());
    }

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("pacienteIdSet")
    default Set<PacienteDTO> toDtoPacienteIdSet(Set<Paciente> paciente) {
        return paciente.stream().map(this::toDtoPacienteId).collect(Collectors.toSet());
    }

    @Named("direccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DireccionDTO toDtoDireccionId(Direccion direccion);

    @Named("direccionIdSet")
    default Set<DireccionDTO> toDtoDireccionIdSet(Set<Direccion> direccion) {
        return direccion.stream().map(this::toDtoDireccionId).collect(Collectors.toSet());
    }
}
