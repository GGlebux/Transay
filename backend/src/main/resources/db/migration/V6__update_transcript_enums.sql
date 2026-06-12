UPDATE public.transcript
SET gender = UPPER(gender);

ALTER TABLE public.transcript
    ADD CONSTRAINT chk_gender
        CHECK (gender IN ('MALE', 'FEMALE', 'BOTH'));