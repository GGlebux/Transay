package project.assay.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "http_log")
@Data
@NoArgsConstructor
public class HttpLog {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "method")
    private String method;

    @Column(name = "uri")
    private String uri;

    @Column(name = "query_string")
    private String queryString;

    @Column(name = "status")
    private int status;

    @Column(name = "duration_ms")
    private long durationMs;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "principal")
    private String principal;

    @Column(name = "request_body", columnDefinition = "text")
    private String requestBody;

    @Column(name = "response_body", columnDefinition = "text")
    private String responseBody;

    @Column(name = "error")
    private boolean error;
}
