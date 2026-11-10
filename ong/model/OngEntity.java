package io.github.go.gerenciamento_adocoes.ong.model;

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
@Table(name = "ong")
@Getter
@Setter
public class OngEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "phone")
    private String phone;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "ong", fetch = FetchType.LAZY)
    private List<AdocaoEntity> adocoes = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        setRegistrationDate(LocalDateTime.now());
    }
}
