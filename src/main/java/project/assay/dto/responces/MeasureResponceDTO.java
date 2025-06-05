package project.assay.dto.responces;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.assay.models.Reason;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class MeasureResponceDTO {
  private int id;
  private double minValue;
  private double currentValue;
  private double maxValue;
  private LocalDate regDate;
  private String units;
  private String status;
  private List<Reason> reasons;
}
