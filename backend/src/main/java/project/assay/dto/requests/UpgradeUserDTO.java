package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.assay.models.enums.CustomerRole;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Сущность для повышения прав пользователя")
public class UpgradeUserDTO {
    @Schema(description = "id пользователя",
            requiredMode = REQUIRED,
            examples = {"1", "2", "3"}
    )
    private long id;

    @Schema(description = "Роль",
            requiredMode = REQUIRED,
            examples = {"EDITOR", "USER"}
    )
    private CustomerRole role;
}
