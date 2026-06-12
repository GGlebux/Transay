package project.assay.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@Schema(description = "Сущность для передачи информации во время возникновения ошибок - отлавливать!")
public class ProblemDetailResponse {
    private String type;
    private String title;
    private int status;
    @Schema(description = "Тут текстовое сообщение!")
    private String detail;
    private String instance;
    private Map<String, Object> properties;

    public ProblemDetailResponse(String title, int status, String detail, String instance) {
        this.type = "about:blank";
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }
}
