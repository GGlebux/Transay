package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import project.assay.utils.converters.JsonToListConverter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureUpdateDTO {
  // Поле для определения id выбранного индикатора (indicatorId)
  private int celectedId;

  @NotNull(message = "currentValue shoud not be empty")
  private double currentValue;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate regDate;

}
