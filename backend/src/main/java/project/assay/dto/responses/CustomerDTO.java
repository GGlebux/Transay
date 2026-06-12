package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import project.assay.models.enums.CustomerRole;
import project.assay.models.enums.CustomerStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class CustomerDTO {
    @Schema(description = "Id пользователя")
    private Long id;

    @Schema(description = "Электронная почта")
    private String email;

    @Schema(description = "Статус пользователя",
            examples = {"PENDING", "ACTIVE"})
    private CustomerStatus status;

    @Schema(description = "Роль пользователя",
            examples = {"USER", "EDITOR", "ADMIN"})
    private CustomerRole role;

    @Schema(description = "Дата создания пользователя",
            example = "2023-10-15T13:45:30")
    private LocalDateTime createdAt;

    @Schema(description = "Подтвержден?")
    private boolean isVerified;

    @Schema(description = "Id человека (личные данные)",
            example = "1")
    private Long personId;
}
