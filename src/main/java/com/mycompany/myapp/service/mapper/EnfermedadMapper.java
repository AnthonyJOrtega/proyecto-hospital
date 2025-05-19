package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Enfermedad;
import com.mycompany.myapp.domain.Informe;
import com.mycompany.myapp.service.dto.EnfermedadDTO;
import com.mycompany.myapp.service.dto.InformeDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enfermedad} and its DTO {@link EnfermedadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnfermedadMapper extends EntityMapper<EnfermedadDTO, Enfermedad> {
    @Mapping(target = "informes", source = "informes", qualifiedByName = "informeIdSet")
    EnfermedadDTO toDto(Enfermedad s);

    @Mapping(target = "informes", ignore = true)
    @Mapping(target = "removeInforme", ignore = true)
    Enfermedad toEntity(EnfermedadDTO enfermedadDTO);

    @Named("informeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InformeDTO toDtoInformeId(Informe informe);

    @Named("informeIdSet")
    default Set<InformeDTO> toDtoInformeIdSet(Set<Informe> informe) {
        return informe.stream().map(this::toDtoInformeId).collect(Collectors.toSet());
    }
}
