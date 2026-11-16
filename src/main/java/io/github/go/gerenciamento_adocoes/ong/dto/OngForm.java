package io.github.go.gerenciamento_adocoes.ong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OngForm(@NotBlank(message = "Campo obrigatório")
                         String name,
                      String cnpj,
                      @NotNull(message = "Campo obrigatório")
                         String email,
                      @NotNull(message = "Campo obrigatório")
                         String contact,
                      @NotNull(message = "Campo obrigatório")
                          String phone) {


}
