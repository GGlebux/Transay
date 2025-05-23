INSERT INTO person(name, gender, date_of_birth) VALUES ('GGLEBUX', 'male', '2006-08-16');


INSERT INTO transcript (name, fall, raise)
VALUES ('hemoglobin',
        '[
          "дефицит белка",
          "дефицит меди",
          "дефицит марганца",
          "дефицит витамина С",
          "дефицит витамина В1",
          "дефицит витамина В9",
          "дефицит витамина В12",
          "недостаточное поступление железа и/или его плохое усвоение",
          "скрытые/явные кровопотери",
          "инфекция Helicobacter pylori",
          "беременность",
          "эрозии и язвы в желудке"
        ]'::jsonb,
        '[
          "показатель гипоксии",
          "обезвоживание",
          "переутомление",
          "курение",
          "синдром раздраженного кишечника",
          "сахарный диабет"
        ]'::jsonb);

INSERT INTO transcript (name, fall, raise)
VALUES ('hematocrit',
        '[
          "анемии любого рода",
          "скрытые и явные кровопотери",
          "беременность",
          "гипергидратация"
        ]'::jsonb,
        '[
          "гиперпротеинемия",
          "отеки из-за нарушения функции почек",
          "обезвоживание организма",
          "гематологические заболевания"
        ]'::jsonb);

INSERT INTO transcript (name, fall, raise)
VALUES ('erythrocyte',
        '[
          "скрытое воспаление",
          "дефицит витамина В6",
          "дефицит витамина В9",
          "дефицит витамина В12",
          "заболевания почек (при этом будут отклонения в креатинине)",
          "беременность",
          "токсичные металлы",
          "катаболизм"
        ]'::jsonb,
        '[
          "дефицит железа",
          "истинная полицитемия",
          "обезвоживание",
          "курение"
        ]'::jsonb);

insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'both', 6, 0, 220, 180, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'both', 29, 7, 180, 150, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin.', 'both', 179, 30, 120, 90, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'both', 1824, 180, 140, 120, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'both', 4379, 1825, 150, 125, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'female', 54750, 4380, 160, 125, false);
insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hemoglobin', 'male', 54750, 4381, 170, 130, false);

insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('erythrocyte', 'male', 54750, 4381, 5.5, 4.5, false);

insert into indicator(name, gender, max_age, min_age, max_value, min_value, is_gravid)
VALUES ('hematocrit', 'male', 54750, 4381, 54, 40, false);