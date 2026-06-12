package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Сущность для утверждение нового пароля")
public class NewPassDTO {
    @NotNull(message = "token не может быть пустым!")
    @Schema(description = "Токен из письма",
            requiredMode = REQUIRED,
            example = "jkrj4223kgoroqp45435"
    )
    private String token;

    @NotNull(message = "newPassword не может быть пустым!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать буквы верхнего и нижнего регистра, цифру, спецсимвол, без пробелов!")
    @Schema(description = "Новый пароль",
            requiredMode = REQUIRED,
            example = "SUjfs234*31!"
    )
    private String newPassword;
}
