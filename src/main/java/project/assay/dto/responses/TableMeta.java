package project.assay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Data
@AllArgsConstructor
public class TableMeta implements Comparable<TableMeta> {
    @Override
    public int compareTo(@NotNull TableMeta o) {
        return this.indicatorName.compareTo(o.indicatorName);
    }

    private String indicatorName;
    private Set<MeasureResponceDTO> measures;
}
