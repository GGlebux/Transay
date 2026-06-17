-- Семья: у одного аккаунта может быть несколько людей.
-- У person появляется владелец-customer. Существующие "свои" анкеты
-- (на которые ссылается customer.person_id) тоже привязываем к владельцу.

ALTER TABLE public.person ADD COLUMN owner_id bigint;

ALTER TABLE public.person
    ADD CONSTRAINT person_owner_id_fkey
        FOREIGN KEY (owner_id) REFERENCES public.customer(id) ON DELETE CASCADE;

UPDATE public.person p
   SET owner_id = c.id
  FROM public.customer c
 WHERE c.person_id = p.id;

CREATE INDEX idx_person_owner_id ON public.person (owner_id);
