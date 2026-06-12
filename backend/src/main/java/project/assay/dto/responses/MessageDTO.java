package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сущность сообщения")
public class MessageDTO {
    @Schema(description = "Просто сообщение об успехе",
            example = "Операция успешно выполнена!")
    private String detail;
}
