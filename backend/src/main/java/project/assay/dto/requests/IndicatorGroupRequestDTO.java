package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.assay.dto.SimpleIndicatorDTO;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность группы индикаторов (создание)")
public class IndicatorGroupRequestDTO {
    @NotNull(message = "Название группы не может быть пустым!")
    @Schema(description = "Название группы",
            requiredMode = REQUIRED,
            examples = {"Общий анализ крови", "Биохимический анализ крови", "Белковый обмен", "Липидограмма"}
    )
    private String groupName;

    @NotNull(message = "Список имён индикаторов не может быть пустым!")
    @Schema(description = "Список индикаторов с единицами измерения",
            requiredMode = REQUIRED
    )
    private List<SimpleIndicatorDTO> indicators;
}

