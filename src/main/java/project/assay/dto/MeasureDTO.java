package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureDTO {
  private int id;

  private double minValue;

  @NotNull(message = "currentValue should not be empty")
  private double currentValue;

  private double maxValue;

  private String minAge;

  private String maxAge;

  private String status;

  @Convert(converter = JsonToListConverter.class)
  private List<String> reasons;
}
