package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Data
@AllArgsConstructor
@Schema(description = "Сущность всех измерений одного индикатора (строчка таблицы)")
public class TableMeta implements Comparable<TableMeta> {
    @Override
    public int compareTo(@NotNull TableMeta o) {
        return this.indicatorName.compareTo(o.indicatorName);
    }

    @Schema(description = "Имя индикатора",
            example = "Гемоглобин")
    private String indicatorName;
    @Schema(description = "Список существующих измерений")
    private Set<MeasureResponseDTO> measures;
}
