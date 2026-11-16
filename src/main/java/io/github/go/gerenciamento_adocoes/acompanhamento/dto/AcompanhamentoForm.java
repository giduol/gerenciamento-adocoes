package io.github.go.gerenciamento_adocoes.acompanhamento.dto;

import java.util.Set;

import io.github.go.gerenciamento_adocoes.acompanhamento.model.MeioAcompanhamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcompanhamentoForm {

    private Integer periodDays;

    private Boolean animalWithAdopter;

    private Boolean animalHealthy;

    private Boolean animalReturned;

    private Boolean animalNotFound;

    private Set<MeioAcompanhamento> methods;

    private String notes;
}
