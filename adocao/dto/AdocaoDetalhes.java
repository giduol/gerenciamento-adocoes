package io.github.go.gerenciamento_adocoes.adocao.dto;

import java.time.LocalDateTime;

import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.adocao.model.AdoptionStatus;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;

public record AdocaoDetalhes(
        Long id,
        OngDetalhes ong,
        AnimalDetalhes animal,
        AdotanteDetalhes adotante,
        byte[] adoptionDocument,
        LocalDateTime adoptionDate,
        AdoptionStatus status
) {}