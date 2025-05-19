package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Direccion;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.DireccionDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Direccion} and its DTO {@link DireccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DireccionMapper extends EntityMapper<DireccionDTO, Direccion> {
    @Mapping(target = "pacientes", source = "pacientes", qualifiedByName = "pacienteIdSet")
    @Mapping(target = "trabajadors", source = "trabajadors", qualifiedByName = "trabajadorIdSet")
    DireccionDTO toDto(Direccion s);

    @Mapping(target = "removePaciente", ignore = true)
    @Mapping(target = "removeTrabajador", ignore = true)
    Direccion toEntity(DireccionDTO direccionDTO);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("pacienteIdSet")
    default Set<PacienteDTO> toDtoPacienteIdSet(Set<Paciente> paciente) {
        return paciente.stream().map(this::toDtoPacienteId).collect(Collectors.toSet());
    }

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("trabajadorIdSet")
    default Set<TrabajadorDTO> toDtoTrabajadorIdSet(Set<Trabajador> trabajador) {
        return trabajador.stream().map(this::toDtoTrabajadorId).collect(Collectors.toSet());
    }
}
