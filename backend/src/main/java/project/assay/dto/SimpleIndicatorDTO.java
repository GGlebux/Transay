package project.assay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Минимальный вид индикатора")
public class SimpleIndicatorDTO{
    @NotNull(message = "Название индикатора не может быть пустым!")
    @Schema(description = "Название индикатора",
            requiredMode = REQUIRED,
            examples = {"Гемоглобин", "Общий белок", "Ферритин"}
    )
    private String name;

    @NotNull(message = "Единицы измерения индикатора не могут быть пустым!")
    @Schema(description = "Единицы измерения",
            requiredMode = REQUIRED,
            examples = {"г/л", "тера/л", "фл", "пг"}
    )
    private List<String> units;
}