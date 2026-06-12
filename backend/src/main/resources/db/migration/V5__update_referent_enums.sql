UPDATE public.referent
SET status = UPPER(status);

ALTER TABLE public.referent
ADD CONSTRAINT chk_status
CHECK ( status in ('FALL', 'OK', 'RAISE') );
