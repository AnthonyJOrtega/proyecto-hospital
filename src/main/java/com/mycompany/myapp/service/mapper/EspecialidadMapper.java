package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Especialidad;
import com.mycompany.myapp.domain.Trabajador;
import com.mycompany.myapp.service.dto.EspecialidadDTO;
import com.mycompany.myapp.service.dto.TrabajadorDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Especialidad} and its DTO {@link EspecialidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EspecialidadMapper extends EntityMapper<EspecialidadDTO, Especialidad> {
    @Mapping(target = "trabajadors", source = "trabajadors", qualifiedByName = "trabajadorIdSet")
    EspecialidadDTO toDto(Especialidad s);

    @Mapping(target = "trabajadors", ignore = true)
    @Mapping(target = "removeTrabajador", ignore = true)
    Especialidad toEntity(EspecialidadDTO especialidadDTO);

    @Named("trabajadorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "idUsuario", source = "idUsuario")
    TrabajadorDTO toDtoTrabajadorId(Trabajador trabajador);

    @Named("trabajadorIdSet")
    default Set<TrabajadorDTO> toDtoTrabajadorIdSet(Set<Trabajador> trabajador) {
        return trabajador.stream().map(this::toDtoTrabajadorId).collect(Collectors.toSet());
    }
}
