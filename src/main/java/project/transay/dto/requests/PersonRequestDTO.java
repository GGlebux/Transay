package project.transay.dto.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.transay.models.ConditionEnum;
import project.transay.models.GenderEnum;

import java.time.LocalDate;

@Data
public class PersonRequestDTO  {
    @Size(min = 2, max = 100, message = "Имя должно быть в диапазоне от 2 до 100 символов!")
    @NotEmpty(message = "Имя не может быть пустым!")
    private String name;

    @NotNull(message = "Гендер не может быть пустым!")
    private GenderEnum gender;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @NotNull(message = "Дата рождения не может быть пустой!")
    private LocalDate dateOfBirth;

    @NotNull(message = "Состояние не может быть пустым!")
    private ConditionEnum condition;
}
