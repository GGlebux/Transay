package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

@Data
@Builder(toBuilder = true)
public class MeasureDTO {
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
