package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import project.assay.models.AgeRange;
import project.assay.models.enums.Condition;
import project.assay.models.enums.IndicatorGender;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность полного представления индикатора (созданная)")
public class IndicatorResponseDTO {
    @Schema(description = "Id индикатора")
    private int id;
    
    @Schema(description = "Название на английском языке",
            examples = {"MCH", "PLT", "Ferritin"})
    private String engName;

    @NotNull(message = "rus_name не может быть пустым!")
    @Schema(description = "Название на русском языке",
            examples = {"Гемоглобин", "Общий белок", "Ферритин"})
    private String rusName;

    @Pattern(regexp = "^(MALE|FEMALE|BOTH)$", message = "Гендер должен быть 'MALE' или 'FEMALE' или 'BOTH'")
    @Schema(description = "Пол того, кому предназначен индикатор",
            requiredMode = REQUIRED,
            examples = {"MALE", "FEMALE", "BOTH"}
    )
    private IndicatorGender gender;

    @NotNull(message = "condition не может быть пустым!")
    @Schema(description = "Беременна ли?",
            requiredMode = REQUIRED,
            examples = {"BASE", "GRAVID", "MENSES"}
    )
    private Condition condition;

    @NotNull(message = "minAge не может быть пустым!")
    @Schema(description = "Минимальный возраст в днях",
            examples = {"123", "353", "6235"})
    private AgeRange minAge;

    @NotNull(message = "maxAge не может быть пустым!")
    @Schema(description = "Максимальный возраст в днях",
            examples = {"123", "353", "6235"})
    private AgeRange maxAge;

    @NotNull(message = "minValue не может быть пустым!")
    @Schema(description = "Нижний порог нормы",
            examples = {"0.234", "314", "160"})
    private double minValue;

    @NotNull(message = "maxValue не может быть пустым!")
    @Schema(description = "Верхний порог нормы",
            examples = {"0.234", "314", "160"})
    private double maxValue;

    @Schema(description = "Единицы измерения",
            requiredMode = NOT_REQUIRED,
            examples = {"г/л", "тера/л", "фл", "пг"})
    private String units;
}
