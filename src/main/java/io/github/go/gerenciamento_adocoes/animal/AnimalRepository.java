
package io.github.go.gerenciamento_adocoes.animal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import io.github.go.gerenciamento_adocoes.animal.model.AnimalEntity;
import io.github.go.gerenciamento_adocoes.ong.model.OngEntity;

public interface AnimalRepository extends JpaRepository<AnimalEntity, Long> {

    Optional<OngEntity> findByName(String name);

    @Query("""
            SELECT c
            FROM AnimalEntity c
            WHERE (:id IS NULL OR c.id <> :id)
            AND c.name = :name
            """)
    List<AnimalEntity> findByNameAndNotId(
            @Param("name") String name,
            @Param("id") Long id
    );
}
