package project.assay.dto.requests;

import lombok.Data;
import project.assay.dto.SimpleIndicatorDTO;

import java.util.List;

@Data
public class IndicatorGroupRequestDTO {
    private String groupName;
    private List<SimpleIndicatorDTO> indicators;
}

