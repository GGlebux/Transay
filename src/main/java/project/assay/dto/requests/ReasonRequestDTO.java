package project.assay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReasonRequestDTO {
    @NotNull(message = "Причина не может быть пустой!")
    private String name;
}
