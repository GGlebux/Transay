package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Schema(description = "Сущность сводной таблицы измерений для одной группы индикаторов")
public class SummaryTableGroupDTO implements Comparable<SummaryTableGroupDTO> {
    @Override
    public int compareTo(@NotNull SummaryTableGroupDTO o) {
        return this.groupName.compareTo(o.groupName);
    }

    @Schema(description = "Название группы",
            examples = {"Общий анализ крови", "Биохимический анализ крови", "Белковый обмен", "Липидограмма"}
    )
    private String groupName;

    @Schema(description = "Даты - шапка таблицы")
    private Set<LocalDate> dates;

    @Schema(description = "Строки таблицы")
    private Set<TableMeta> metas;
}
