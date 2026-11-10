package io.github.go.gerenciamento_adocoes.ong.dto;

import java.time.LocalDateTime;

public record OngDetalhes(Long id,
                          String name,
                          String cnpj,
                          String email,
                          String contact,
                          String phone,
                          LocalDateTime registrationDate){
}
