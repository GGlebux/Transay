package project.assay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SimpleIndicatorDTO{
    private String name;
    private List<String> units;
}