package project.assay.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
@Schema(description = "Сущность электронной почты")
public class EmailDTO {
    @NotNull(message = "Адрес электронной почты не может быть пустым!")
    @Email(message = "Неверный адрес электронной почты!")
    @Schema(requiredMode = REQUIRED,
            example = "rejngleb@gmail.com")
    private String email;
}
