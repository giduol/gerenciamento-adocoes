package io.github.go.gerenciamento_adocoes.acompanhamento.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "acompanhamento")
@Getter
@Setter
public class AcompanhamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adoption_id", nullable = false)
    private AdocaoEntity adoption;

    @Column(name = "period_days", nullable = false)
    private Integer periodDays;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    @Column(name = "animal_with_adopter")
    private Boolean animalWithAdopter;

    @Column(name = "animal_healthy")
    private Boolean animalHealthy;

    @Column(name = "animal_returned")
    private Boolean animalReturned;

    @Column(name = "animal_not_found")
    private Boolean animalNotFound;

    @Column(name = "methods", nullable = false, columnDefinition = "TEXT")
    private String methodsJson = "[]";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "media_files", nullable = false, columnDefinition = "JSONB")
    private JsonNode mediaFiles;
}
