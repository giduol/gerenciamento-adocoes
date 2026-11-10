package io.github.go.gerenciamento_adocoes.adotante.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdotanteForm(@NotBlank(message = "Campo obrigatório")
                         String name,
                           @NotNull(message = "Campo obrigatório")
                         String cpf,
                           @NotNull(message = "Campo obrigatório")
                         String address,
                           @NotNull(message = "Campo obrigatório")
                         String email,
                           @NotNull(message = "Campo obrigatório")
                         String phone) {


}
