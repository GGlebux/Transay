package project.assay.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorWithCurrentValue {
  private Indicator indicator;
  private Double currentValue;
}

