package io.github.go.gerenciamento_adocoes.adotante.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;

@Entity
@Table(name = "adotante")
@Getter
@Setter
public class AdotanteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "adotante", fetch = FetchType.LAZY)
    private List<AdocaoEntity> adocoes = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        setRegistrationDate(LocalDateTime.now());
    }
}
