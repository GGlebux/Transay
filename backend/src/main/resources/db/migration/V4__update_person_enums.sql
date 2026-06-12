UPDATE public.person
SET gender = UPPER(gender);

ALTER TABLE public.person
ADD CONSTRAINT chk_gender
CHECK (gender IN ('MALE', 'FEMALE'));

ALTER TABLE public.person
    ADD COLUMN condition VARCHAR(6);

UPDATE public.person
SET condition = CASE
                    WHEN is_gravid = true THEN 'GRAVID'
                    ELSE 'BASE'
    END;

ALTER TABLE public.person
    ALTER COLUMN condition SET NOT NULL;

ALTER TABLE public.person
    DROP COLUMN is_gravid;