package project.assay.dto.responces;

import lombok.Data;

@Data
public class SimpleIndicatorResponceDTO {
    private int id;
    private String name;
    private double minValue;
    private double maxValue;
}
