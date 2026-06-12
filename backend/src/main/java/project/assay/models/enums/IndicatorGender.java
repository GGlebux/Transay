package project.assay.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IndicatorGender {
    MALE("Мужской"),
    FEMALE("Женский"),
    BOTH("Обоеполый");

    private final String value;

    public static PersonGender convert(IndicatorGender gender) {
        return PersonGender.valueOf(gender.getValue());
    }
}
