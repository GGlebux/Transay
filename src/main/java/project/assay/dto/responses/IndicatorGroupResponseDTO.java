package project.assay.dto.responses;

import lombok.Data;
import project.assay.dto.SimpleIndicatorDTO;

import java.util.List;

@Data
public class IndicatorGroupResponseDTO {
    private int id;
    private String groupName;
    private List<SimpleIndicatorDTO> indicators;
}

