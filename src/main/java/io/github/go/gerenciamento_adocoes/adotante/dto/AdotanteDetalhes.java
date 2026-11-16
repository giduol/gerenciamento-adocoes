package io.github.go.gerenciamento_adocoes.adotante.dto;

import java.time.LocalDateTime;

public record AdotanteDetalhes(Long id,
                               String name,
                               String cpf,
                               String address,
                               String email,
                               String phone,
                               LocalDateTime registrationDate){
}
