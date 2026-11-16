package io.github.go.gerenciamento_adocoes.common.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Registro não encontrado");
    }
}
