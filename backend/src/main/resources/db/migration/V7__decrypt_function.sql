-- Расшифровка (топ-причины) одним запросом.
-- Повторяет логику MeasureService.getDecryptedMeasures / createDecryptMap:
--   * берём измерения человека на дату,
--   * у отклонённых референтов (status <> 'OK') собираем причины из
--     fall_reason / raise_reason соответствующих транскрипций (снимок referent_transcript),
--   * исключаем причины из excluded_reason человека,
--   * группируем по причине: matchesCount = число индикаторов, indicators = их имена,
--   * percentage = доля от суммы совпадений (исправленная формула),
--   * сортируем по matchesCount DESC, ключ результата = имя причины.
-- Возвращает json (а не jsonb) намеренно: json сохраняет порядок ключей,
-- что воспроизводит LinkedHashMap из Java.
CREATE OR REPLACE FUNCTION get_decrypt(p_person_id bigint, p_reg_date date)
    RETURNS json
    LANGUAGE sql
    STABLE
AS
$$
WITH verdict AS (
    -- одна строка на пару (измерение, причина) после дедупликации,
    -- без причин, исключённых человеком
    SELECT DISTINCT m.id       AS measure_id,
                    i.rus_name AS indicator_name,
                    rs.id      AS reason_id,
                    rs.name    AS reason_name
    FROM measure m
             JOIN referent r ON r.id = m.referent_id
             JOIN indicator i ON i.id = m.indicator_id
             JOIN referent_transcript rt ON rt.referent_id = r.id
             JOIN (SELECT transcript_id, reason_id, 'FALL' AS st FROM fall_reason
                   UNION ALL
                   SELECT transcript_id, reason_id, 'RAISE' AS st FROM raise_reason) fr
                  ON fr.transcript_id = rt.transcript_id AND fr.st = r.status
             JOIN reason rs ON rs.id = fr.reason_id
    WHERE m.person_id = p_person_id
      AND r.reg_date = p_reg_date
      AND r.status <> 'OK'
      AND NOT EXISTS (SELECT 1
                      FROM excluded_reason er
                      WHERE er.person_id = p_person_id
                        AND er.reason_id = rs.id)),
     agg AS (SELECT reason_id,
                    reason_name,
                    count(*)                                              AS matches_count,
                    json_agg(DISTINCT indicator_name ORDER BY indicator_name) AS indicators
             FROM verdict
             GROUP BY reason_id, reason_name),
     total AS (SELECT sum(matches_count) AS s FROM agg)
SELECT coalesce(
               json_object_agg(
                       a.reason_name,
                       json_build_object(
                               'reasonId', a.reason_id,
                               'matchesCount', a.matches_count,
                               'percentage', (a.matches_count::float8 / t.s),
                               'indicators', a.indicators
                       )
                       ORDER BY a.matches_count DESC, a.reason_id
               ),
               '{}'::json
       )
FROM agg a
         CROSS JOIN total t;
$$;
