package project.assay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.assay.models.HttpLog;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogDetailDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String method;
    private String uri;
    private String queryString;
    private int status;
    private long durationMs;
    private String clientIp;
    private String principal;
    private String requestBody;
    private String responseBody;
    private boolean error;

    public static HttpLogDetailDTO from(HttpLog log) {
        return new HttpLogDetailDTO(
                log.getId(),
                log.getCreatedAt(),
                log.getMethod(),
                log.getUri(),
                log.getQueryString(),
                log.getStatus(),
                log.getDurationMs(),
                log.getClientIp(),
                log.getPrincipal(),
                log.getRequestBody(),
                log.getResponseBody(),
                log.isError()
        );
    }
}
