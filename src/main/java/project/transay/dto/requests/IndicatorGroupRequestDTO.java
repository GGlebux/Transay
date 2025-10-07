package project.transay.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.transay.dto.SimpleIndicatorDTO;

import java.util.List;

@Data
public class IndicatorGroupRequestDTO {
    @NotNull(message = "Название группы не может быть пустым!")
    private String groupName;
    @NotNull(message = "Список имён индикаторов не может быть пустым!")
    private List<SimpleIndicatorDTO> indicators;
}

