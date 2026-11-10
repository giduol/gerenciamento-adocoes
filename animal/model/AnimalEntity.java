package io.github.go.gerenciamento_adocoes.animal.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "animal")
@Getter
@Setter
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "species", nullable = false, length = 30)
    private String species;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "breed")
    private String breed;

    @Column(name = "special_characteristics")
    private String specialCharacteristics;

    @Column(name = "known_diseases")
    private String knownDiseases;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "animal", fetch = FetchType.LAZY)
    private List<AdocaoEntity> adocoes = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        setRegistrationDate(LocalDateTime.now());
    }
}
