package project.assay.dto.responces;

import lombok.Data;

@Data
public class SimpleIndicatorResponceDTO {
    private int id;
    private String rusName;
    private String units;
    private double minValue;
    private double maxValue;
}
