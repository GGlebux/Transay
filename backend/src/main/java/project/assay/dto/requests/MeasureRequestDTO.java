package project.assay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@JsonInclude(NON_NULL)
@Schema(description = "Сущность измерения (связь индикатора и человека)")
public class MeasureRequestDTO {
  @NotNull(message = "name не может быть пустым!")
  @Schema(description = "Имя индикатора",
          requiredMode = REQUIRED,
          examples = {"Гемоглобин", "Общий белок", "Ферритин"}
  )
  private String name;

  @NotNull(message = "currentValue не может быть пустым!")
  @Schema(description = "Текущее значение индикатора",
          requiredMode = REQUIRED,
          examples = {"4.23", "343", "573"}
  )
  private double currentValue;

  @NotNull(message = "units не может быть пустым!")
  @Schema(description = "Единицы измерения",
          requiredMode = REQUIRED,
          examples = {"г/л", "тера/л", "фл", "пг"}
  )
  private String units;

  @JsonFormat(pattern = "dd.MM.yyyy")
  @NotNull(message = "regDate не может быть пустым!")
  @Schema(description = "Дата сбора анализа",
          requiredMode = REQUIRED,
          example = "16.03.2025"
  )
  private LocalDate regDate;
}
