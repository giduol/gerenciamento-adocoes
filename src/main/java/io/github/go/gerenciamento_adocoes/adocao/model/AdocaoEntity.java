package io.github.go.gerenciamento_adocoes.adocao.model;

import java.time.LocalDateTime;

import io.github.go.gerenciamento_adocoes.adotante.model.AdotanteEntity;
import io.github.go.gerenciamento_adocoes.animal.model.AnimalEntity;
import io.github.go.gerenciamento_adocoes.ong.model.OngEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adocao")
@Getter
@Setter
public class AdocaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ong_id", nullable = false)
    private OngEntity ong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalEntity animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adotante_id", nullable = false)
    private AdotanteEntity adotante;

    @Column(name = "adoption_date", nullable = false)
    private LocalDateTime adoptionDate;

    @Column(name = "adoption_document", columnDefinition = "BYTEA")
    private byte[] adoptionDocument;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;

    @PrePersist
    public void prePersist() {
        if (adoptionDate == null) {
            adoptionDate = LocalDateTime.now();
        }
    }
}