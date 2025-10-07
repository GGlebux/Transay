package project.transay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.transay.models.GenderEnum;

import java.util.Set;

@Data
public class TranscriptRequestDTO {
    @NotNull(message = "Имя не может быть пустым!")
    private String name;

    @NotNull(message = "Гендер не может быть пустым!")
    private GenderEnum gender;

    private Set<Integer> fallsIds;
    private Set<Integer> raisesIds;
}
