package project.assay.dto;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class IndicatorDTO implements Comparable<IndicatorDTO>{
  @Override
  public int compareTo(@NotNull IndicatorDTO o) {
    return this.name.compareTo(o.name);
  }

  private int id;

  private String name;

  private double minValue;

  private double maxValue;
}
