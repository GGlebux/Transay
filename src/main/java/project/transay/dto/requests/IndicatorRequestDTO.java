package project.transay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.transay.models.AgeRange;
import project.transay.models.ConditionEnum;
import project.transay.models.GenderEnum;

@Data
public class IndicatorRequestDTO {
    @NotNull(message = "Английское название не может быть пустым!")
    private String engName;

    @NotNull(message = "Русское название не может быть пустым!")
    private String rusName;

    @NotNull(message = "Гендер не может быть пустым!")
    private GenderEnum gender;

    @NotNull(message = "Состояние не может быть пустым!")
    private ConditionEnum condition;

    @NotNull(message = "Минимальный возраст не может быть пустым!")
    private AgeRange minAge;

    @NotNull(message = "Максимальный возраст не может быть пустым!")
    private AgeRange maxAge;

    @NotNull(message = "Минимальное значение не может быть пустым!")
    private double minValue;

    @NotNull(message = "Максимальное значение не может быть пустым!")
    private double maxValue;

    @NotNull(message = "Единицы измерения не могут быть пустыми!")
    private String units;
}
