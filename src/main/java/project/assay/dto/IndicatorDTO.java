package project.assay.dto;

import lombok.Data;

@Data
public class IndicatorDTO {
  private int id;

  private String name;

  private double minValue;

  private double maxValue;
}
