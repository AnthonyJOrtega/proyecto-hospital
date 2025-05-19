package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.domain.Paciente;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.domain.Trabajador;
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
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "trabajador", source = "trabajador", qualifiedByName = "trabajadorId")
    @Mapping(target = "enfermedads", source = "enfermedads", qualifiedByName = "enfermedadIdSet")
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
}
