package project.assay.utils.responces;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonErrorResponse {
    private String errorMessage;
    private LocalDateTime timestamp;

    public PersonErrorResponse(String errorMessage, LocalDateTime timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
