package project.assay.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Condition {
    BASE("Базовый"),
    GRAVID("Беременность"),
    MENSES("Менструация");

    private final String value;
}
