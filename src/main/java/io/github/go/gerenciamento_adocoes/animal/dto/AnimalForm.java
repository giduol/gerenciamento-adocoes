package io.github.go.gerenciamento_adocoes.animal.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

public record AnimalForm(@NotBlank(message = "Campo obrigatório")
                         String name,
                         String species,
                         LocalDate birthDate,
                         String breed,
                         String specialCharacteristics,
                         String knownDiseases) {


}
