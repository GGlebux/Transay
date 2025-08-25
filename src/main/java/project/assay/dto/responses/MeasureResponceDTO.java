package project.assay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import project.assay.models.Reason;

import java.time.LocalDate;
import java.util.Set;

import static java.lang.Integer.compare;

@Data
@AllArgsConstructor
public class MeasureResponceDTO implements Comparable<MeasureResponceDTO>{
  private int id;
  private double minValue;
  private double currentValue;
  private double maxValue;
  private LocalDate regDate;
  private String units;
  private String status;
  private Set<Reason> reasons;

  @Override
  public int compareTo(@NotNull MeasureResponceDTO o) {
    return this.regDate.compareTo(o.regDate);
  }

  public void clearExReasons(Set<Reason> exReasons) {
    this.reasons.removeAll(exReasons);
  }
}
