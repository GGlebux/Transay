package project.assay.utils.responces;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonErrorResponce {
    private String errorMessage;
    private LocalDateTime timestamp;

    public PersonErrorResponce(String errorMessage, LocalDateTime timestamp) {
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
