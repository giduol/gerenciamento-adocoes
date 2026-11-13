package io.github.go.gerenciamento_adocoes.animal.mapper;

import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.github.go.gerenciamento_adocoes.animal.model.AnimalEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-12T17:47:39-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4 (Oracle Corporation)"
)
@Component
public class AnimalMapperImpl implements AnimalMapper {

    @Override
    public AnimalEntity toEntity(AnimalForm form) {
        if ( form == null ) {
            return null;
        }

        AnimalEntity animalEntity = new AnimalEntity();

        animalEntity.setSpecies( form.species() );
        animalEntity.setName( form.name() );
        animalEntity.setBirthDate( form.birthDate() );
        animalEntity.setBreed( form.breed() );
        animalEntity.setSpecialCharacteristics( form.specialCharacteristics() );
        animalEntity.setKnownDiseases( form.knownDiseases() );

        return animalEntity;
    }

    @Override
    public AnimalDetalhes toDetalhes(AnimalEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String species = null;
        String name = null;
        LocalDate birthDate = null;
        String breed = null;
        String specialCharacteristics = null;
        String knownDiseases = null;
        LocalDateTime registrationDate = null;

        id = entity.getId();
        species = entity.getSpecies();
        name = entity.getName();
        birthDate = entity.getBirthDate();
        breed = entity.getBreed();
        specialCharacteristics = entity.getSpecialCharacteristics();
        knownDiseases = entity.getKnownDiseases();
        registrationDate = entity.getRegistrationDate();

        AnimalDetalhes animalDetalhes = new AnimalDetalhes( id, species, name, birthDate, breed, specialCharacteristics, knownDiseases, registrationDate );

        return animalDetalhes;
    }

    @Override
    public void updateEntity(AnimalEntity entity, AnimalForm dadosAtualizados) {
        if ( dadosAtualizados == null ) {
            return;
        }

        entity.setSpecies( dadosAtualizados.species() );
        entity.setName( dadosAtualizados.name() );
        entity.setBirthDate( dadosAtualizados.birthDate() );
        entity.setBreed( dadosAtualizados.breed() );
        entity.setSpecialCharacteristics( dadosAtualizados.specialCharacteristics() );
        entity.setKnownDiseases( dadosAtualizados.knownDiseases() );
    }
}
