package project.transay.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class SummaryTableGroupDTO implements Comparable<SummaryTableGroupDTO> {
    @Override
    public int compareTo(@NotNull SummaryTableGroupDTO o) {
        return this.groupName.compareTo(o.groupName);
    }

    private String groupName;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private Set<LocalDate> dates;
    private Set<TableMeta> metas;
}
