-- Сводная таблица одним запросом.
-- Повторяет MeasureService.createSummaryTable / createSetMeasuresByName / createSetOfTableGroups:
--   * берём ВСЕ измерения человека (все даты, любой статус),
--   * для каждого измерения вердикт (reasons) собираем из LIVE-транскрипций
--     по eng_name индикатора и полу человека (gender = пол ИЛИ 'BOTH'),
--     причины fall_reason/raise_reason по статусу, минус excluded_reason человека,
--   * раскладываем измерения по группам indicator_group (имя = часть строки до '|'),
--   * dates группы = отсортированные уникальные даты измерений индикаторов группы,
--   * группа попадает в ответ только если в ней есть хотя бы один индикатор человека.
-- Сортировки воспроизводят TreeSet из Java: группы по groupName, строки по indicatorName,
-- измерения по regDate, причины по id. Возвращает json (сохраняет порядок ключей/массивов).
CREATE OR REPLACE FUNCTION get_summary_table(p_person_id bigint)
    RETURNS json
    LANGUAGE sql
    STABLE
AS
$$
WITH pers AS (SELECT id, gender FROM person WHERE id = p_person_id),
     -- все измерения человека с данными индикатора и референта
     meas AS (SELECT m.id AS measure_id,
                     i.eng_name,
                     i.rus_name,
                     i.units,
                     i.min_value,
                     i.max_value,
                     r.current_value,
                     r.reg_date,
                     r.status
              FROM measure m
                       JOIN referent r ON r.id = m.referent_id
                       JOIN indicator i ON i.id = m.indicator_id
              WHERE m.person_id = p_person_id),
     -- вердикт (причины) на каждое отклонённое измерение: live-транскрипции по eng_name+пол,
     -- минус исключённые причины человека
     reasons_per_measure AS (SELECT mm.measure_id,
                                    json_agg(json_build_object('id', rs.id, 'name', rs.name)
                                             ORDER BY rs.id) AS reasons
                             FROM meas mm
                                      CROSS JOIN pers p
                                      JOIN transcript t
                                           ON t.name = mm.eng_name AND (t.gender = p.gender OR t.gender = 'BOTH')
                                      JOIN (SELECT transcript_id, reason_id, 'FALL' AS st FROM fall_reason
                                            UNION ALL
                                            SELECT transcript_id, reason_id, 'RAISE' AS st FROM raise_reason) fr
                                           ON fr.transcript_id = t.id AND fr.st = mm.status
                                      JOIN reason rs ON rs.id = fr.reason_id
                             WHERE mm.status <> 'OK'
                               AND NOT EXISTS (SELECT 1
                                               FROM excluded_reason er
                                               WHERE er.person_id = p_person_id
                                                 AND er.reason_id = rs.id)
                             GROUP BY mm.measure_id),
     -- каждое измерение как объект MeasureResponseDTO
     measure_json AS (SELECT mm.measure_id,
                             mm.rus_name,
                             mm.reg_date,
                             json_build_object(
                                     'id', mm.measure_id,
                                     'minValue', mm.min_value,
                                     'currentValue', mm.current_value,
                                     'maxValue', mm.max_value,
                                     'regDate', to_char(mm.reg_date, 'YYYY-MM-DD'),
                                     'units', mm.units,
                                     'status', mm.status,
                                     'reasons', coalesce(rpm.reasons, '[]'::json)
                             ) AS obj
                      FROM meas mm
                               LEFT JOIN reasons_per_measure rpm ON rpm.measure_id = mm.measure_id),
     -- развёрнутые членства групп: (group_id, group_name, имя индикатора)
     grp AS (SELECT DISTINCT ig.id AS group_id, ig.group_name, split_part(elem, '|', 1) AS member_name
             FROM indicator_group ig,
                  unnest(ig.indicators) AS elem),
     -- строки таблицы: на (группа, индикатор человека) — отсортированные измерения
     metas AS (SELECT g.group_id,
                      mj.rus_name AS indicator_name,
                      json_build_object(
                              'indicatorName', mj.rus_name,
                              'measures', json_agg(mj.obj ORDER BY mj.reg_date)
                      )           AS meta_obj
               FROM grp g
                        JOIN measure_json mj ON mj.rus_name = g.member_name
               GROUP BY g.group_id, mj.rus_name),
     -- даты-шапка на группу
     group_dates AS (SELECT g.group_id,
                            g.group_name,
                            json_agg(DISTINCT to_char(mj.reg_date, 'YYYY-MM-DD')
                                     ORDER BY to_char(mj.reg_date, 'YYYY-MM-DD')) AS dates
                     FROM grp g
                              JOIN measure_json mj ON mj.rus_name = g.member_name
                     GROUP BY g.group_id, g.group_name),
     -- сборка групп (только непустые)
     groups AS (SELECT gd.group_name,
                       json_build_object(
                               'groupName', gd.group_name,
                               'dates', gd.dates,
                               'metas', (SELECT json_agg(me.meta_obj ORDER BY me.indicator_name)
                                         FROM metas me
                                         WHERE me.group_id = gd.group_id)
                       ) AS group_obj
                FROM group_dates gd)
SELECT coalesce(json_agg(g.group_obj ORDER BY g.group_name), '[]'::json)
FROM groups g;
$$;
