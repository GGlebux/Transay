package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность подсчитанной причины (для круговой диаграммы)")
public class DecryptValueDTO{
    @Schema(description = "Id причины",
            examples = {"2", "4", "64"})
    private int reasonId;

    @Schema(description = "Количество совпадений",
            examples = {"43", "4", "1"})
    private int matchesCount;

    @Schema(description = "Процентное соотношения относительно всех причин",
            examples = {"43.34", "3", "1"})
    private double percentage;

    @Schema(description = "Список названий индикаторов")
    private Set<String> indicators;
}
