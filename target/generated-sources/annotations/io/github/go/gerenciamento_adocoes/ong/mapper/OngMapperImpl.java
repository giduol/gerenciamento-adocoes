package io.github.go.gerenciamento_adocoes.ong.mapper;

import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.ong.model.OngEntity;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-12T17:47:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (Oracle Corporation)"
)
@Component
public class OngMapperImpl implements OngMapper {

    @Override
    public OngEntity toEntity(OngForm form) {
        if ( form == null ) {
            return null;
        }

        OngEntity ongEntity = new OngEntity();

        ongEntity.setName( form.name() );
        ongEntity.setCnpj( form.cnpj() );
        ongEntity.setEmail( form.email() );
        ongEntity.setContact( form.contact() );
        ongEntity.setPhone( form.phone() );

        return ongEntity;
    }

    @Override
    public OngDetalhes toDetalhes(OngEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String name = null;
        Long id = null;
        String cnpj = null;
        String email = null;
        String contact = null;
        String phone = null;
        LocalDateTime registrationDate = null;

        name = entity.getName();
        id = entity.getId();
        cnpj = entity.getCnpj();
        email = entity.getEmail();
        contact = entity.getContact();
        phone = entity.getPhone();
        registrationDate = entity.getRegistrationDate();

        OngDetalhes ongDetalhes = new OngDetalhes( id, name, cnpj, email, contact, phone, registrationDate );

        return ongDetalhes;
    }

    @Override
    public void updateEntity(OngEntity entity, OngForm dadosAtualizados) {
        if ( dadosAtualizados == null ) {
            return;
        }

        entity.setName( dadosAtualizados.name() );
        entity.setCnpj( dadosAtualizados.cnpj() );
        entity.setEmail( dadosAtualizados.email() );
        entity.setContact( dadosAtualizados.contact() );
        entity.setPhone( dadosAtualizados.phone() );
    }
}
