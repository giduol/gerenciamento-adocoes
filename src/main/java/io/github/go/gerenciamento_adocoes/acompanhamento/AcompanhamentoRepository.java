package io.github.go.gerenciamento_adocoes.acompanhamento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import io.github.go.gerenciamento_adocoes.acompanhamento.model.AcompanhamentoEntity;

public interface AcompanhamentoRepository extends JpaRepository<AcompanhamentoEntity, Long> {

    @EntityGraph(attributePaths = {"adoption", "adoption.animal", "adoption.adotante"})
    List<AcompanhamentoEntity> findByAdoptionIdOrderByPerformedAtAsc(Long adocaoId);

    @Override
    @EntityGraph(attributePaths = {"adoption", "adoption.animal", "adoption.adotante"})
    Optional<AcompanhamentoEntity> findById(Long id);
}
