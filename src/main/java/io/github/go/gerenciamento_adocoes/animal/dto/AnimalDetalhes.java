package io.github.go.gerenciamento_adocoes.animal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AnimalDetalhes(Long id,
                             String species,
                             String name,
                             LocalDate birthDate,
                             String breed,
                             String specialCharacteristics,
                             String knownDiseases,
                             LocalDateTime registrationDate){
}
