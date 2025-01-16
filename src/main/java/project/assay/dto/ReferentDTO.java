package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

@Data
public class ReferentDTO {
  private int id;

  private String name;

  private double minValue;

  @NotNull(message = "currentValue shoud not be empty")
  private double currentValue;

  private double maxValue;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate regDate;

  private String units;

  private String status;

  @Convert(converter = JsonToListConverter.class)
  private List<String> description;

}
