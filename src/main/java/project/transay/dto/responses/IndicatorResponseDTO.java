package project.transay.dto.responses;

import lombok.Data;
import project.transay.models.AgeRange;
import project.transay.models.ConditionEnum;
import project.transay.models.GenderEnum;

@Data
public class IndicatorResponseDTO {
    private int id;
    private String engName;
    private String rusName;
    private GenderEnum gender;
    private ConditionEnum condition;
    private AgeRange minAge;
    private AgeRange maxAge;
    private double minValue;
    private double maxValue;
    private String units;
}
