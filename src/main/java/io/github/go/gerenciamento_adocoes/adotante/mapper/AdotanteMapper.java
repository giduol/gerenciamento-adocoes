package io.github.go.gerenciamento_adocoes.adotante.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.model.AdotanteEntity;

@Mapper(componentModel = "spring")
public interface AdotanteMapper {
    @Mapping(target = "name", source = "name")
    AdotanteEntity toEntity(AdotanteForm form);

    @Mapping(target = "name", source = "name")
    AdotanteDetalhes toDetalhes(AdotanteEntity entity);

    @Mapping(target = "name", source = "name")
    void updateEntity(@MappingTarget AdotanteEntity entity, AdotanteForm dadosAtualizados);
}
