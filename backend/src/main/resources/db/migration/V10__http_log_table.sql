CREATE TABLE IF NOT EXISTS http_log
(
    id            BIGSERIAL PRIMARY KEY,
    created_at    TIMESTAMP   NOT NULL DEFAULT now(),
    method        VARCHAR(10) NOT NULL,
    uri           TEXT        NOT NULL,
    query_string  TEXT,
    status        INTEGER     NOT NULL,
    duration_ms   BIGINT      NOT NULL,
    client_ip     VARCHAR(64),
    principal     VARCHAR(255),
    request_body  TEXT,
    response_body TEXT,
    error         BOOLEAN     NOT NULL DEFAULT false
);

CREATE INDEX IF NOT EXISTS idx_http_log_created_at ON http_log (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_http_log_status ON http_log (status);
