package project.assay.dto;

import jakarta.persistence.Convert;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

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

  @Convert(converter = JsonToListConverter.class)
  private List<String> reasons;
}
