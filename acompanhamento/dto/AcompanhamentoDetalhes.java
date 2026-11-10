package io.github.go.gerenciamento_adocoes.acompanhamento.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import io.github.go.gerenciamento_adocoes.acompanhamento.model.MeioAcompanhamento;

public record AcompanhamentoDetalhes(
        Long id,
        Long adoptionId,
        Long adopterId,
        String adopterName,
        Long animalId,
        String animalName,
        Integer periodDays,
        LocalDateTime performedAt,
        Boolean animalWithAdopter,
        Boolean animalHealthy,
        Boolean animalReturned,
        Boolean animalNotFound,
        Set<MeioAcompanhamento> methods,
        List<MediaFileDetails> mediaFiles,
        String notes
) {}
