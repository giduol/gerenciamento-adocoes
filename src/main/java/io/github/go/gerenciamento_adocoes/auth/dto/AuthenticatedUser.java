package io.github.go.gerenciamento_adocoes.auth.dto;

import java.util.List;

public record AuthenticatedUser(String username, List<String> papeis) {}
