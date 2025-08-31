package project.assay.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimpleIndicatorDTO{
    @NotNull(message = "Название индикатора не может быть пустым!")
    private String name;
    @NotNull(message = "Единицы измерения индикатора не могут быть пустым!")
    private List<String> units;
}