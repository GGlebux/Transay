package project.assay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.assay.models.enums.Condition;
import project.assay.models.enums.PersonGender;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность создания человека (личные данные пользователя)")
public class PersonRequestDTO  {
    @Size(min = 2, max = 50, message = "имя должно быть в диапазоне от 2 до 50 символов!")
    @NotEmpty(message = "name не может быть пустым!")
    @Schema(description = "Имя человека",
            requiredMode = REQUIRED,
            examples = {"Глеб", "Никита"}
    )
    private String name;

    @Schema(description = "Пол",
            requiredMode = REQUIRED,
            examples = {"MALE", "FEMALE"})
    private PersonGender gender;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull(message = "Date_of_birthday не может быть пустым!")
    @Schema(description = "Дата рождения",
            requiredMode = REQUIRED,
            example = "12.03.2000")
    private LocalDate dateOfBirth;

    @NotNull(message = "condition не может быть пустым!")
    @Schema(description = "Состояние человека",
            examples = {"BASE", "GRAVID", "MENSES"})
    private Condition condition;
}
