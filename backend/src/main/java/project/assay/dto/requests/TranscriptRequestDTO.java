package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность транскрипции (причины повышения и понижения для конкретного индикатора)")
public class TranscriptRequestDTO {
    @NotNull(message = "name не может быть пустым!")
    @Schema(description = "Имя индикатора",
            requiredMode = REQUIRED,
            examples = {"Гемоглобин", "Общий белок", "Ферритин"}
    )
    private String name;

    @NotNull(message = "gender не может быть пустым!")
    @Schema(description = "Пол",
            requiredMode = REQUIRED,
            examples = {"male", "female", "both"}
    )
    private String gender;

    @Schema(description = "Список id причин понижения")
    private Set<Integer> fallsIds;

    @Schema(description = "Список id причин понижения")
    private Set<Integer> raisesIds;
}
