package io.github.go.gerenciamento_adocoes.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record Credentials(
        @NotBlank String username,
        @NotBlank String password
) {}
