package io.github.go.gerenciamento_adocoes.common.exceptions;

import java.util.List;

import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;

public class ValidationException extends RuntimeException{

    private List<CampoInvalido> camposInvalidos;

    public ValidationException(List<CampoInvalido> camposInvalidos) {
        super("Erro de validação.");
        this.camposInvalidos = camposInvalidos;
    }

    public List<CampoInvalido> getCamposInvalidos() {
        return camposInvalidos;
    }
}
