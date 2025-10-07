package project.transay.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import project.transay.models.Reason;
import project.transay.models.ReferentStatusEnum;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class MeasureResponseDTO implements Comparable<MeasureResponseDTO> {
    private int id;
    private double minValue;
    private double currentValue;
    private double maxValue;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate regDate;
    private String units;
    private ReferentStatusEnum status;
    private Set<Reason> reasons;

    @Override
    public int compareTo(@NotNull MeasureResponseDTO o) {
        return this.regDate.compareTo(o.regDate);
    }

    public void clearExReasons(Set<Reason> exReasons) {
        this.reasons.removeAll(exReasons);
    }
}
