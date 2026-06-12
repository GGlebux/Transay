package project.assay.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PersonGender {
    MALE("Мужской"),
    FEMALE("Женский");

    private final String value;
}
