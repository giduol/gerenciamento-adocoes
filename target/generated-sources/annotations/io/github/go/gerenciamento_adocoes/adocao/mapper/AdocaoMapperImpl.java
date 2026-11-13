package io.github.go.gerenciamento_adocoes.adocao.mapper;

import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoDetalhes;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import io.github.go.gerenciamento_adocoes.adocao.model.AdoptionStatus;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.mapper.AdotanteMapper;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.mapper.AnimalMapper;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.github.go.gerenciamento_adocoes.ong.mapper.OngMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-12T17:47:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (Oracle Corporation)"
)
@Component
public class AdocaoMapperImpl implements AdocaoMapper {

    @Autowired
    private OngMapper ongMapper;
    @Autowired
    private AnimalMapper animalMapper;
    @Autowired
    private AdotanteMapper adotanteMapper;

    @Override
    public AdocaoEntity toEntity(AdocaoForm form) {
        if ( form == null ) {
            return null;
        }

        AdocaoEntity adocaoEntity = new AdocaoEntity();

        byte[] adoptionDocument = form.getAdoptionDocument();
        if ( adoptionDocument != null ) {
            adocaoEntity.setAdoptionDocument( Arrays.copyOf( adoptionDocument, adoptionDocument.length ) );
        }
        adocaoEntity.setStatus( form.getStatus() );

        return adocaoEntity;
    }

    @Override
    public AdocaoDetalhes toDetalhes(AdocaoEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        OngDetalhes ong = null;
        AnimalDetalhes animal = null;
        AdotanteDetalhes adotante = null;
        byte[] adoptionDocument = null;
        LocalDateTime adoptionDate = null;
        AdoptionStatus status = null;

        id = entity.getId();
        ong = ongMapper.toDetalhes( entity.getOng() );
        animal = animalMapper.toDetalhes( entity.getAnimal() );
        adotante = adotanteMapper.toDetalhes( entity.getAdotante() );
        byte[] adoptionDocument1 = entity.getAdoptionDocument();
        if ( adoptionDocument1 != null ) {
            adoptionDocument = Arrays.copyOf( adoptionDocument1, adoptionDocument1.length );
        }
        adoptionDate = entity.getAdoptionDate();
        status = entity.getStatus();

        AdocaoDetalhes adocaoDetalhes = new AdocaoDetalhes( id, ong, animal, adotante, adoptionDocument, adoptionDate, status );

        return adocaoDetalhes;
    }

    @Override
    public void updateEntity(AdocaoEntity entity, AdocaoForm dadosAtualizados) {
        if ( dadosAtualizados == null ) {
            return;
        }

        byte[] adoptionDocument = dadosAtualizados.getAdoptionDocument();
        if ( adoptionDocument != null ) {
            entity.setAdoptionDocument( Arrays.copyOf( adoptionDocument, adoptionDocument.length ) );
        }
        else {
            entity.setAdoptionDocument( null );
        }
        entity.setStatus( dadosAtualizados.getStatus() );
    }
}
