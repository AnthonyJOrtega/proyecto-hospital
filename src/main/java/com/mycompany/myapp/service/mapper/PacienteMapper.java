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
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    @Mapping(target = "trabajadors", source = "trabajadors", qualifiedByName = "trabajadorIdSet")
    @Mapping(target = "direccions", source = "direccions", qualifiedByName = "direccionIdSet")
    PacienteDTO toDto(Paciente s);

    @Mapping(target = "removeTrabajador", ignore = true)
    @Mapping(target = "direccions", ignore = true)
    @Mapping(target = "removeDireccion", ignore = true)
    Paciente toEntity(PacienteDTO pacienteDTO);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("trabajadorIdSet")
    default Set<TrabajadorDTO> toDtoTrabajadorIdSet(Set<Trabajador> trabajador) {
        return trabajador.stream().map(this::toDtoTrabajadorId).collect(Collectors.toSet());
    }

    @Named("direccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "calle", source = "calle")
    @Mapping(target = "numero", source = "numero")
    @Mapping(target = "ciudad", source = "ciudad")
    @Mapping(target = "codigoPostal", source = "codigoPostal")
    @Mapping(target = "pais", source = "pais")
    DireccionDTO toDtoDireccionId(Direccion direccion);

    @Named("direccionIdSet")
    default Set<DireccionDTO> toDtoDireccionIdSet(Set<Direccion> direccion) {
        return direccion.stream().map(this::toDtoDireccionId).collect(Collectors.toSet());
    }
}
