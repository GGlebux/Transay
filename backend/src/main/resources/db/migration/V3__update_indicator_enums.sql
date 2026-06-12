UPDATE public.indicator
SET gender = UPPER(gender);

ALTER TABLE public.indicator
ADD CONSTRAINT chk_gender
CHECK (gender IN ('MALE', 'FEMALE', 'BOTH'));

ALTER TABLE public.indicator
ADD COLUMN condition VARCHAR(6);

UPDATE public.indicator
SET condition = CASE
    WHEN is_gravid = true THEN 'GRAVID'
    ELSE 'BASE'
END;

ALTER TABLE public.indicator
ALTER COLUMN condition SET NOT NULL;

ALTER TABLE public.indicator
DROP COLUMN is_gravid;