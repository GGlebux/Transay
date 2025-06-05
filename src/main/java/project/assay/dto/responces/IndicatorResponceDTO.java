package project.assay.dto.responces;

import lombok.Data;
import project.assay.models.AgeRange;

@Data
public class IndicatorResponceDTO {
    private int id;
    private String eng_name;
    private String rus_name;
    private String gender;
    private boolean isGravid;
    private AgeRange minAge;
    private AgeRange maxAge;
    private double minValue;
    private double maxValue;
    private String units;
}
