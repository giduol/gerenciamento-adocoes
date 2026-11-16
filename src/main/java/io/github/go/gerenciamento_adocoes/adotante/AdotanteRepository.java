
package io.github.go.gerenciamento_adocoes.adotante;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.go.gerenciamento_adocoes.adotante.model.AdotanteEntity;

public interface AdotanteRepository extends JpaRepository<AdotanteEntity, Long> {

    Optional<AdotanteEntity> findByName(String name);

    @Query("""
            SELECT c
            FROM AdotanteEntity c
            WHERE (:id IS NULL OR c.id <> :id)
            AND c.name = :name
            """)
    List<AdotanteEntity> findByNameAndNotId(
            @Param("name") String name,
            @Param("id") Long id
    );
}
