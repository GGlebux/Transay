package project.transay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class MeasureRequestDTO {
  @NotNull(message = "Название не может быть пустым!")
  private String name;

  @NotNull(message = "Текущее значение не может быть пустым!")
  private double currentValue;

  @NotNull(message = "Единицы измерения не могут быть пустыми!")
  private String units;

  @JsonFormat(pattern = "dd.MM.yyyy")
  @NotNull(message = "Дата анализа не может быть пустой!")
  private LocalDate regDate;
}
