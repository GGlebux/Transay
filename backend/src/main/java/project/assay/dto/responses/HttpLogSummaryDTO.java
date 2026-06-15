package project.assay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.assay.models.HttpLog;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpLogSummaryDTO {
    private Long id;
    private LocalDateTime createdAt;
    private String method;
    private String uri;
    private int status;
    private long durationMs;
    private boolean error;

    public static HttpLogSummaryDTO from(HttpLog log) {
        return new HttpLogSummaryDTO(
                log.getId(),
                log.getCreatedAt(),
                log.getMethod(),
                log.getUri(),
                log.getStatus(),
                log.getDurationMs(),
                log.isError()
        );
    }
}
