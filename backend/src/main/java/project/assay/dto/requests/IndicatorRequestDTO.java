package project.assay.dto.requests;

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
@Schema(description = "Сущность полного представления индикатора (создание)")
public class IndicatorRequestDTO {
    @NotNull(message = "eng_name не может быть пустым!")
    @Schema(description = "Название на английском языке",
            requiredMode = REQUIRED,
            examples = {"MCH", "PLT", "Ferritin"}
    )
    private String engName;

    @NotNull(message = "rus_name не может быть пустым!")
    @Schema(description = "Название на русском языке",
            requiredMode = REQUIRED,
            examples = {"Гемоглобин", "Общий белок", "Ферритин"}
    )
    private String rusName;

    @Schema(description = "Пол того, кому предназначен индикатор",
            requiredMode = REQUIRED,
            examples = {"MALE", "FEMALE", "BOTH"}
    )
    private IndicatorGender gender;

    @Schema(description = "Беременна ли?",
            requiredMode = REQUIRED,
            examples = {"BASE", "GRAVID", "MENSES"}
    )
    private Condition condition;

    @NotNull(message = "minAge не может быть пустым!")
    @Schema(description = "Минимальный возраст в днях",
            requiredMode = REQUIRED,
            examples = {"123", "353", "6235"}
    )
    private AgeRange minAge;

    @NotNull(message = "maxAge не может быть пустым!")
    @Schema(description = "Максимальный возраст в днях",
            requiredMode = REQUIRED,
            examples = {"123", "353", "6235"}
    )
    private AgeRange maxAge;

    @NotNull(message = "minValue не может быть пустым!")
    @Schema(description = "Нижний порог нормы",
            requiredMode = REQUIRED,
            examples = {"0.234", "314", "160"}
    )
    private double minValue;

    @NotNull(message = "maxValue не может быть пустым!")
    @Schema(description = "Верхний порог нормы",
            requiredMode = REQUIRED,
            examples = {"0.234", "314", "160"}
    )
    private double maxValue;

    @Schema(description = "Единицы измерения",
            requiredMode = NOT_REQUIRED,
            examples = {"г/л", "тера/л", "фл", "пг"}
    )
    private String units;
}
