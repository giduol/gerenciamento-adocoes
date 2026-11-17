package io.github.go.gerenciamento_adocoes.acompanhamento.dto;

import java.util.List;

public record AcompanhamentosResponse(List<AcompanhamentoComAdocaoDetalhes> acompanhamentos) {
    public AcompanhamentosResponse {
        acompanhamentos = List.copyOf(acompanhamentos);
    }
}
