package project.assay.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class MeasureResponceDTO {
  private int id;
  private double minValue;
  private double currentValue;
  private double maxValue;
  private LocalDate regDate;
  private String units;
  private String status;
  private List<String> reasons;
}
