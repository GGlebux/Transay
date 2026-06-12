package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import project.assay.models.Reason;
import project.assay.models.enums.ReferentStatus;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@Schema(description = "Сущность измерения (созданная)")
public class MeasureResponseDTO implements Comparable<MeasureResponseDTO>{
  private int id;

  @Schema(description = "Нижний порог нормы",
          examples = {"0.234", "314", "160"})
  private double minValue;

  @Schema(description = "Текущее значение индикатора",
          examples = {"4.23", "343", "573"})
  private double currentValue;

  @Schema(description = "Верхний порог нормы",
          examples = {"0.234", "314", "160"})
  private double maxValue;
  
  @Schema(description = "Дата сдачи анализа",
          examples = "15.05.2024")
  private LocalDate regDate;

  @Schema(description = "Единицы измерения",
          examples = {"г/л", "тера/л", "фл", "пг"})
  private String units;

  @Schema(description = "Статус текущего значения",
        examples = {"FALL", "RAISE", "OK"})
  private ReferentStatus status;

  @Schema(description = "Список причин повышения/понижения")
  private Set<Reason> reasons;

  @Override
  public int compareTo(@NotNull MeasureResponseDTO o) {
    return this.regDate.compareTo(o.regDate);
  }

  public void clearExReasons(Set<Reason> exReasons) {
    this.reasons.removeAll(exReasons);
  }
}
