package io.github.go.gerenciamento_adocoes.adocao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;

import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import io.github.go.gerenciamento_adocoes.adocao.model.AdoptionStatus;

public interface AdocaoRepository extends JpaRepository<AdocaoEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"ong", "animal", "adotante"})
    List<AdocaoEntity> findAll();

    List<AdocaoEntity> findByAnimalName(String name);

    List<AdocaoEntity> findByAdotanteCpf(String cpf);

    List<AdocaoEntity> findByOngId(Long ongId);

    List<AdocaoEntity> findByStatus(AdoptionStatus status);

    @Query("""
            SELECT a
            FROM AdocaoEntity a
            WHERE (:id IS NULL OR a.id <> :id)
            AND a.animal.id = :animalId
            """)
    List<AdocaoEntity> findByAnimalIdAndNotId(
            @Param("animalId") Long animalId,
            @Param("id") Long id
    );
}