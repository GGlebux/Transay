package project.assay.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonErrorResponce {
    private String message;
    private LocalDateTime timestamp;

    public PersonErrorResponce(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
