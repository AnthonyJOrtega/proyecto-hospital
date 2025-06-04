package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.dto.InformeDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "informe", source = "informe", qualifiedByName = "informeId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteNombreApellido")
    @Mapping(target = "trabajadors", source = "trabajadors", qualifiedByName = "trabajadorNombreApellidoSet")
    CitaDTO toDto(Cita s);

    @Mapping(target = "removeTrabajador", ignore = true)
    Cita toEntity(CitaDTO citaDTO);

    @Named("informeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InformeDTO toDtoInformeId(Informe informe);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    // Este mapeo es para obtener el nombre y apellido del paciente
    @Named("pacienteNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    PacienteDTO toDtoPacienteNombreApellido(Paciente paciente);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    // Este mapeo es para obtener el id de los trabajadores
    @Named("trabajadorNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "idUsuario", source = "idUsuario")
    @Mapping(target = "especialidads", source = "especialidads")
    TrabajadorDTO toDtoTrabajadorNombreApellido(Trabajador trabajador);

    // Este mapeo es para obtener el id de los trabajadores en un Set
    @Named("trabajadorNombreApellidoSet")
    default Set<TrabajadorDTO> toDtoTrabajadorNombreApellidoSet(Set<Trabajador> trabajadors) {
        return trabajadors.stream().map(this::toDtoTrabajadorNombreApellido).collect(Collectors.toSet());
    }
}
