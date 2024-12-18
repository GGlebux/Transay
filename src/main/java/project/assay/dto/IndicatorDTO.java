package project.assay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Data;

@Data
public class IndicatorDTO {

  private int id;

  @NotEmpty(message = "Name should not be empty")
  private String name;

  @NotEmpty(message = "Gender should not be empty")
  private String gender;

  private Map<String, Integer> maxAge;

  private Map<String, Integer> minAge;

  private double maxValue;

  private double minValue;

  @NotNull(message = "Name should not be empty")
  private double currentValue;
}
