package io.github.go.gerenciamento_adocoes.acompanhamento.dto;

import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoDetalhes;

public record AcompanhamentoComAdocaoDetalhes(
        AcompanhamentoDetalhes acompanhamento,
        AdocaoDetalhes adocao
) {}
