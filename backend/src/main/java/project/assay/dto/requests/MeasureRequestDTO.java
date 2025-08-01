package project.assay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class MeasureRequestDTO {
  // Поле для определения id выбранного индикатора (indicatorId)
  private int selectedId;

  @NotNull(message = "currentValue shoud not be empty")
  private double currentValue;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate regDate;
}
