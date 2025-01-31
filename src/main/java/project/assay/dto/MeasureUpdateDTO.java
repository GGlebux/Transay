package project.assay.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureUpdateDTO {
  // Поле для определения id выбранного индикатора (indicatorId)
  private int selectedId;

  @NotNull(message = "currentValue shoud not be empty")
  private double currentValue;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate regDate;

}
