package io.github.go.gerenciamento_adocoes.acompanhamento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.github.go.gerenciamento_adocoes.acompanhamento.model.AcompanhamentoEntity;

public interface AcompanhamentoRepository extends JpaRepository<AcompanhamentoEntity, Long> {

    @EntityGraph(attributePaths = {"adoption", "adoption.ong", "adoption.animal", "adoption.adotante"})
    List<AcompanhamentoEntity> findByAdoptionIdOrderByPerformedAtAsc(Long adocaoId);

    @EntityGraph(attributePaths = {"adoption", "adoption.ong", "adoption.animal", "adoption.adotante"})
    List<AcompanhamentoEntity> findByAdoptionAdotanteCpfOrderByPerformedAtAsc(String cpf);

    @Override
    @EntityGraph(attributePaths = {"adoption", "adoption.ong", "adoption.animal", "adoption.adotante"})
    Optional<AcompanhamentoEntity> findById(Long id);

    @Modifying
    @Query("DELETE FROM AcompanhamentoEntity a WHERE a.adoption.id = :adocaoId")
    void deleteByAdoptionId(@Param("adocaoId") Long adocaoId);
}
