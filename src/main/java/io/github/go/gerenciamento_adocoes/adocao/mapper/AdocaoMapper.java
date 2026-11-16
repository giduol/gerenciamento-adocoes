package io.github.go.gerenciamento_adocoes.adocao.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.github.go.gerenciamento_adocoes.adotante.mapper.AdotanteMapper;
import io.github.go.gerenciamento_adocoes.animal.mapper.AnimalMapper;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoDetalhes;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import io.github.go.gerenciamento_adocoes.ong.mapper.OngMapper;

@Mapper(componentModel = "spring", uses = {OngMapper.class, AnimalMapper.class, AdotanteMapper.class})
public interface AdocaoMapper {
    AdocaoEntity toEntity(AdocaoForm form);

    AdocaoDetalhes toDetalhes(AdocaoEntity entity);

    void updateEntity(@MappingTarget AdocaoEntity entity, AdocaoForm dadosAtualizados);
}
