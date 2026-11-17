package io.github.go.gerenciamento_adocoes.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @EntityGraph(attributePaths = "papeis")
    Optional<UsuarioEntity> findByUsernameIgnoreCase(String username);
}
