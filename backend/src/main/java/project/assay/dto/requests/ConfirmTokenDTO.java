package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Сущность токена верификации")
public class ConfirmTokenDTO {
    @NotNull(message = "Токен не может быть пустым!")
    @Schema(description = "Набор случайных цифр и букв",
            requiredMode = REQUIRED,
            example = "c319c4f4-bca6-4bfc-b292-5a8b669f5dc3"
    )
    private String token;
}
