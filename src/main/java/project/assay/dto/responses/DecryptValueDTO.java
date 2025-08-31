package project.assay.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class DecryptValueDTO{
    private int matchesCount;
    private double percentage;
    private Set<String> indicators;

    public void increment(){
        matchesCount++;
    }

    public void calculateAndSetPercentage(int totalCount){
        percentage = (double) matchesCount / totalCount;
    }
}
