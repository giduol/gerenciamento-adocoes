package io.github.go.gerenciamento_adocoes.ong.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.github.go.gerenciamento_adocoes.ong.model.OngEntity;

@Mapper(componentModel = "spring")
public interface OngMapper {
    @Mapping(target = "name", source = "name")
    OngEntity toEntity(OngForm form);

    @Mapping(target = "name", source = "name")
    OngDetalhes toDetalhes(OngEntity entity);

    @Mapping(target = "name", source = "name")
    void updateEntity(@MappingTarget OngEntity entity, OngForm dadosAtualizados);
}
