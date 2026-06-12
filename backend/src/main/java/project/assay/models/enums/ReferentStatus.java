package project.assay.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReferentStatus {
    FALL("Пониженный"),
    OK("Норма"),
    RAISE("Повышенный");

    public final String value;
}
