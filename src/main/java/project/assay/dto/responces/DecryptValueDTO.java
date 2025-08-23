package project.assay.dto.responces;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class DecryptValueDTO{
    private int matchesCount;
    private Set<String> indicators;

    public void increment(){
        matchesCount++;
    }
}
