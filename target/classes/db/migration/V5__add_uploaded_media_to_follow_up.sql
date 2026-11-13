ALTER TABLE acompanhamento
    ADD COLUMN IF NOT EXISTS media_files JSONB NOT NULL DEFAULT '[]'::jsonb;
