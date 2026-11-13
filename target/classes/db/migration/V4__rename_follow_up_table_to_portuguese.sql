DO $$
BEGIN
    IF to_regclass('acompanhamento') IS NULL
       AND to_regclass('adoption_follow_up') IS NOT NULL THEN
        ALTER TABLE adoption_follow_up RENAME TO acompanhamento;
    END IF;
END $$;
