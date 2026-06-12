package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность refresh-токена")
public class RefreshRequestDTO {
    @Schema(description = "Токен для получения нового access токена - долгосрочный",
            requiredMode = REQUIRED,
            example = "9fab1b29-3381-46f2-91c9-4fb57b2042bb"
    )
    private String refreshToken;
}
