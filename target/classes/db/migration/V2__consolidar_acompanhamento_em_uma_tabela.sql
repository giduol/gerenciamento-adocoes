ALTER TABLE acompanhamento
    ADD COLUMN IF NOT EXISTS periodo_dias INTEGER,
    ADD COLUMN IF NOT EXISTS realizado_em TIMESTAMP,
    ADD COLUMN IF NOT EXISTS meios TEXT NOT NULL DEFAULT '[]',
    ADD COLUMN IF NOT EXISTS referencias_midia TEXT NOT NULL DEFAULT '[]';

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = current_schema()
          AND table_name = 'acompanhamento'
          AND column_name = 'etapa'
    ) THEN
        UPDATE acompanhamento
        SET periodo_dias = CASE etapa
            WHEN 'SETE_DIAS' THEN 7
            WHEN 'TRINTA_DIAS' THEN 30
            WHEN 'SESSENTA_DIAS' THEN 60
            WHEN 'NOVENTA_DIAS' THEN 90
            WHEN 'SEIS_MESES' THEN 180
            WHEN 'UM_ANO' THEN 365
            ELSE NULL
        END
        WHERE periodo_dias IS NULL;
    END IF;
END $$;

DO $$
BEGIN
    IF to_regclass('acompanhamento_meio') IS NOT NULL THEN
        UPDATE acompanhamento a
        SET meios = (
            SELECT COALESCE(json_agg(m.meio)::text, '[]')
            FROM acompanhamento_meio m
            WHERE m.acompanhamento_id = a.id
        );
    END IF;

    IF to_regclass('acompanhamento_midia') IS NOT NULL THEN
        UPDATE acompanhamento a
        SET referencias_midia = (
            SELECT COALESCE(json_agg(m.referencia)::text, '[]')
            FROM acompanhamento_midia m
            WHERE m.acompanhamento_id = a.id
        );
    END IF;
END $$;

DELETE FROM acompanhamento
WHERE periodo_dias IS NULL
   OR (
       realizado_em IS NULL
       AND animal_com_adotante IS NULL
       AND animal_saudavel IS NULL
       AND animal_devolvido IS NULL
       AND animal_nao_encontrado IS NULL
       AND observacoes IS NULL
       AND meios = '[]'
       AND referencias_midia = '[]'
   );

UPDATE acompanhamento
SET realizado_em = CURRENT_TIMESTAMP
WHERE realizado_em IS NULL;

ALTER TABLE acompanhamento
    DROP CONSTRAINT IF EXISTS uk_acompanhamento_adocao_etapa,
    DROP CONSTRAINT IF EXISTS ck_acompanhamento_etapa,
    DROP COLUMN IF EXISTS etapa,
    DROP COLUMN IF EXISTS data_prevista,
    ALTER COLUMN periodo_dias SET NOT NULL,
    ALTER COLUMN realizado_em SET NOT NULL;

ALTER TABLE acompanhamento
    DROP CONSTRAINT IF EXISTS ck_acompanhamento_periodo,
    ADD CONSTRAINT ck_acompanhamento_periodo
        CHECK (periodo_dias IN (7, 30, 60, 90, 180, 365));

DROP TABLE IF EXISTS acompanhamento_meio;
DROP TABLE IF EXISTS acompanhamento_midia;
