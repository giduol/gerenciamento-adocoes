package io.github.go.gerenciamento_adocoes.common.exceptions;

import java.time.Instant;
import java.util.List;

import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;

public record ValidationError(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<CampoInvalido> camposInvalidos
) {}
