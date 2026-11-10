package io.github.go.gerenciamento_adocoes.adocao.dto;

import io.github.go.gerenciamento_adocoes.adocao.model.AdoptionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdocaoForm {

    private Long ongId;

    private Long animalId;

    private Long adotanteId;

    private byte[] adoptionDocument;

    private AdoptionStatus status;
}