CREATE TABLE acompanhamento (
    id BIGSERIAL PRIMARY KEY,
    adocao_id BIGINT NOT NULL REFERENCES adocao(id),
    etapa VARCHAR(20) NOT NULL,
    data_prevista DATE NOT NULL,
    realizado_em TIMESTAMP,
    animal_com_adotante BOOLEAN,
    animal_saudavel BOOLEAN,
    animal_devolvido BOOLEAN,
    animal_nao_encontrado BOOLEAN,
    observacoes TEXT,
    CONSTRAINT uk_acompanhamento_adocao_etapa UNIQUE (adocao_id, etapa),
    CONSTRAINT ck_acompanhamento_etapa CHECK (
        etapa IN ('SETE_DIAS', 'TRINTA_DIAS', 'SESSENTA_DIAS', 'NOVENTA_DIAS', 'SEIS_MESES', 'UM_ANO')
    )
);

CREATE TABLE acompanhamento_meio (
    acompanhamento_id BIGINT NOT NULL REFERENCES acompanhamento(id) ON DELETE CASCADE,
    meio VARCHAR(20) NOT NULL,
    PRIMARY KEY (acompanhamento_id, meio),
    CONSTRAINT ck_acompanhamento_meio CHECK (meio IN ('PRESENCIAL', 'FOTOS', 'VIDEOS'))
);

CREATE TABLE acompanhamento_midia (
    acompanhamento_id BIGINT NOT NULL REFERENCES acompanhamento(id) ON DELETE CASCADE,
    referencia VARCHAR(500) NOT NULL,
    PRIMARY KEY (acompanhamento_id, referencia)
);

INSERT INTO acompanhamento (adocao_id, etapa, data_prevista)
SELECT adocao.id, etapas.etapa, adocao.adoption_date::date + etapas.dias
FROM adocao
CROSS JOIN (
    VALUES
        ('SETE_DIAS', 7),
        ('TRINTA_DIAS', 30),
        ('SESSENTA_DIAS', 60),
        ('NOVENTA_DIAS', 90),
        ('SEIS_MESES', 180),
        ('UM_ANO', 365)
) AS etapas(etapa, dias);
