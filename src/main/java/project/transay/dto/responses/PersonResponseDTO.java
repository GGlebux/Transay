package project.transay.dto.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import project.transay.models.ConditionEnum;
import project.transay.models.GenderEnum;
import project.transay.models.Reason;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class PersonResponseDTO {
    private int id;
    private String name;
    private GenderEnum gender;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;
    private ConditionEnum condition;
    private Set<Reason> excludedReasons = new TreeSet<>();
}
