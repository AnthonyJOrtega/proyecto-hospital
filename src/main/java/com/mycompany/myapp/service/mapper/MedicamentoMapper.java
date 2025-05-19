package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Medicamento;
import com.mycompany.myapp.domain.Receta;
import com.mycompany.myapp.service.dto.MedicamentoDTO;
import com.mycompany.myapp.service.dto.RecetaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicamento} and its DTO {@link MedicamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicamentoMapper extends EntityMapper<MedicamentoDTO, Medicamento> {
    @Mapping(target = "recetas", source = "recetas", qualifiedByName = "recetaIdSet")
    MedicamentoDTO toDto(Medicamento s);

    @Mapping(target = "recetas", ignore = true)
    @Mapping(target = "removeReceta", ignore = true)
    Medicamento toEntity(MedicamentoDTO medicamentoDTO);

    @Named("recetaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RecetaDTO toDtoRecetaId(Receta receta);

    @Named("recetaIdSet")
    default Set<RecetaDTO> toDtoRecetaIdSet(Set<Receta> receta) {
        return receta.stream().map(this::toDtoRecetaId).collect(Collectors.toSet());
    }
}
