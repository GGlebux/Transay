package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "Сущность регистрации")
public class RegisterRequestDTO {
    @NotNull(message = "email не может быть пустым!")
    @Email(message = "Неверный адрес электронной почты!")
    @Schema(description = "Электронная почта",
            requiredMode = REQUIRED,
            example = "rejngleb@gmail.com"
    )
    private String email;

    @NotNull(message = "newPassword не может быть пустым!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?~`])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать буквы верхнего и нижнего регистра, цифру, спецсимвол, без пробелов!")
    @Schema(description = "Пароль",
            requiredMode = REQUIRED,
            example = "JSd4fj3#4"
    )
    private String password;
}
