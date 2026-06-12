package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import project.assay.models.Reason;
import project.assay.models.enums.Condition;
import project.assay.models.enums.PersonGender;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность человека (личные данные пользователя) - созданная")
public class PersonResponseDTO {
    @Schema(description = "Id человека")
    private int id;

    @Schema(description = "Имя человека",
            examples = {"Глеб", "Никита"})
    private String name;

    @Schema(description = "Пол",
            requiredMode = REQUIRED,
            examples = {"MALE", "FEMALE"})
    private PersonGender gender;

    @Schema(description = "Дата рождения",
            requiredMode = REQUIRED,
            example = "12.03.2000")
    private LocalDate dateOfBirth;

    @Schema(description = "Состояние человека",
            examples = {"BASE", "GRAVID", "MENSES"})
    private Condition condition;

    @Schema(description = "Список исключенных причин",
            example = "Например не курит и указал это")
    private Set<Reason> excludedReasons = new TreeSet<>();
}
