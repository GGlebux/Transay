package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import project.assay.dto.SimpleIndicatorDTO;

import java.util.List;

@Data
@Schema(description = "Сущность группы индикаторов (созданная)")
public class IndicatorGroupResponseDTO {
    @Schema(description = "Id группы индикаторов")
    private int id;

    @Schema(description = "Название группы",
            examples = {"Общий анализ крови", "Биохимический анализ крови", "Белковый обмен", "Липидограмма"})
    private String groupName;

    @Schema(description = "Список индикаторов с единицами измерения")
    private List<SimpleIndicatorDTO> indicators;
}

