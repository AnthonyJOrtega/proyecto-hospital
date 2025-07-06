package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Cita;
import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.CitaDTO;
import com.mycompany.myapp.service.dto.EnfermedadDTO;
import com.mycompany.myapp.service.dto.InformeDTO;
import com.mycompany.myapp.service.dto.PacienteDTO;
import com.mycompany.myapp.service.dto.RecetaDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Informe} and its DTO {@link InformeDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformeMapper extends EntityMapper<InformeDTO, Informe> {
    @Mapping(target = "receta", source = "receta", qualifiedByName = "recetaId")
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteNombreApellido")
    @Mapping(target = "trabajador", source = "trabajador", qualifiedByName = "trabajadorNombreApellido")
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadNombreDescripcionSet")
    @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
    InformeDTO toDto(Informe s);

    @Mapping(target = "removeEnfermedad", ignore = true)
    Informe toEntity(InformeDTO informeDTO);

    @Named("recetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RecetaDTO toDtoRecetaId(Receta receta);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("pacienteNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "dni", source = "dni")
    PacienteDTO toDtoPacienteNombreApellido(Paciente paciente);

    @Named("trabajadorNombreApellido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "puesto", source = "puesto")
    @Mapping(target = "especialidads", source = "especialidads")
    @Mapping(target = "idUsuario", source = "idUsuario")
    TrabajadorDTO toDtoTrabajadorNombreApellido(Trabajador trabajador);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("enfermedadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EnfermedadDTO toDtoEnfermedadId(Enfermedad enfermedad);

    @Named("enfermedadIdSet")
    default Set<EnfermedadDTO> toDtoEnfermedadIdSet(Set<Enfermedad> enfermedad) {
        return enfermedad.stream().map(this::toDtoEnfermedadId).collect(Collectors.toSet());
    }

    // Este mapeo es para obtener el nombre y descripcion de la enfermedad
    @Named("enfermedadNombreDescripcion")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "descripcion", source = "descripcion")
    EnfermedadDTO toDtoEnfermedadNombreDescripcion(Enfermedad enfermedad);

    @Named("enfermedadNombreDescripcionSet")
    default Set<EnfermedadDTO> toDtoEnfermedadNombreDescripcionSet(Set<Enfermedad> enfermedads) {
        return enfermedads.stream().map(this::toDtoEnfermedadNombreDescripcion).collect(Collectors.toSet());
    }

    @Named("citaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoCitaId(Cita cita);
}
