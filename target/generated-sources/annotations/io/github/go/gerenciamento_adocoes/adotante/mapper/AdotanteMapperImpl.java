package io.github.go.gerenciamento_adocoes.adotante.mapper;

import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.github.go.gerenciamento_adocoes.adotante.model.AdotanteEntity;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-12T17:47:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (Oracle Corporation)"
)
@Component
public class AdotanteMapperImpl implements AdotanteMapper {

    @Override
    public AdotanteEntity toEntity(AdotanteForm form) {
        if ( form == null ) {
            return null;
        }

        AdotanteEntity adotanteEntity = new AdotanteEntity();

        adotanteEntity.setName( form.name() );
        adotanteEntity.setCpf( form.cpf() );
        adotanteEntity.setEmail( form.email() );
        adotanteEntity.setAddress( form.address() );
        adotanteEntity.setPhone( form.phone() );

        return adotanteEntity;
    }

    @Override
    public AdotanteDetalhes toDetalhes(AdotanteEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String name = null;
        Long id = null;
        String cpf = null;
        String address = null;
        String email = null;
        String phone = null;
        LocalDateTime registrationDate = null;

        name = entity.getName();
        id = entity.getId();
        cpf = entity.getCpf();
        address = entity.getAddress();
        email = entity.getEmail();
        phone = entity.getPhone();
        registrationDate = entity.getRegistrationDate();

        AdotanteDetalhes adotanteDetalhes = new AdotanteDetalhes( id, name, cpf, address, email, phone, registrationDate );

        return adotanteDetalhes;
    }

    @Override
    public void updateEntity(AdotanteEntity entity, AdotanteForm dadosAtualizados) {
        if ( dadosAtualizados == null ) {
            return;
        }

        entity.setName( dadosAtualizados.name() );
        entity.setCpf( dadosAtualizados.cpf() );
        entity.setEmail( dadosAtualizados.email() );
        entity.setAddress( dadosAtualizados.address() );
        entity.setPhone( dadosAtualizados.phone() );
    }
}
