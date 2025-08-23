package project.assay.dto.requests;

import lombok.Data;

import java.util.List;

@Data
public class IndicatorGroupDTO {
    private String groupName;
    private List<String> indicators;
}
