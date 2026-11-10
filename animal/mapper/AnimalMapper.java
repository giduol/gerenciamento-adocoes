package io.github.go.gerenciamento_adocoes.animal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.model.AnimalEntity;

@Mapper(componentModel = "spring")
public interface AnimalMapper {
    AnimalEntity toEntity(AnimalForm form);

    AnimalDetalhes toDetalhes(AnimalEntity entity);

    void updateEntity(@MappingTarget AnimalEntity entity, AnimalForm dadosAtualizados);
}
