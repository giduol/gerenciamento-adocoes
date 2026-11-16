package io.github.go.gerenciamento_adocoes.acompanhamento.model;

public record StoredMediaFile(
        String fileName,
        String contentType,
        String data
) {}
