DO $$
BEGIN
    IF to_regclass('acompanhamento') IS NULL
       AND to_regclass('adoption_follow_up') IS NOT NULL THEN
        ALTER TABLE adoption_follow_up RENAME TO acompanhamento;
    END IF;
END $$;

DO $$
BEGIN
    IF to_regclass('acompanhamento') IS NOT NULL THEN
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'adocao_id') THEN
            ALTER TABLE acompanhamento RENAME COLUMN adocao_id TO adoption_id;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'periodo_dias') THEN
            ALTER TABLE acompanhamento RENAME COLUMN periodo_dias TO period_days;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'realizado_em') THEN
            ALTER TABLE acompanhamento RENAME COLUMN realizado_em TO performed_at;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'animal_com_adotante') THEN
            ALTER TABLE acompanhamento RENAME COLUMN animal_com_adotante TO animal_with_adopter;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'animal_saudavel') THEN
            ALTER TABLE acompanhamento RENAME COLUMN animal_saudavel TO animal_healthy;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'animal_devolvido') THEN
            ALTER TABLE acompanhamento RENAME COLUMN animal_devolvido TO animal_returned;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'animal_nao_encontrado') THEN
            ALTER TABLE acompanhamento RENAME COLUMN animal_nao_encontrado TO animal_not_found;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'meios') THEN
            ALTER TABLE acompanhamento RENAME COLUMN meios TO methods;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'referencias_midia') THEN
            ALTER TABLE acompanhamento RENAME COLUMN referencias_midia TO media_references;
        END IF;
        IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = current_schema() AND table_name = 'acompanhamento' AND column_name = 'observacoes') THEN
            ALTER TABLE acompanhamento RENAME COLUMN observacoes TO notes;
        END IF;

        ALTER TABLE acompanhamento
            DROP CONSTRAINT IF EXISTS uk_acompanhamento_adocao_etapa,
            DROP CONSTRAINT IF EXISTS ck_acompanhamento_etapa,
            DROP CONSTRAINT IF EXISTS ck_acompanhamento_periodo,
            DROP COLUMN IF EXISTS etapa,
            DROP COLUMN IF EXISTS data_prevista;

        UPDATE acompanhamento
        SET methods = replace(replace(methods, '"PRESENCIAL"', '"IN_PERSON"'), '"FOTOS"', '"PHOTOS"')
        WHERE methods IS NOT NULL;

        ALTER TABLE acompanhamento
            ADD CONSTRAINT ck_acompanhamento_period
                CHECK (period_days IN (7, 30, 60, 90, 180, 365));
    END IF;
END $$;
