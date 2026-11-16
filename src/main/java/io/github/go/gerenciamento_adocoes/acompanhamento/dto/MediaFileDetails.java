package io.github.go.gerenciamento_adocoes.acompanhamento.dto;

public record MediaFileDetails(
        Integer index,
        String fileName,
        String contentType,
        Long sizeBytes,
        String downloadUrl
) {}
