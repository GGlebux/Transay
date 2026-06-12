package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность причины")
public class ReasonRequestDTO {
    @NotNull(message = "Причина не может быть пустой!")
    @Schema(description = "Конкретная причина (повышения или понижения показателей)",
            requiredMode = REQUIRED,
            examples = {"дефицит железа", "дефицит белка", "талассемия"}
    )
    private String name;
}
