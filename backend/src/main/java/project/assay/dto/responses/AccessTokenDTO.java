package project.assay.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сущность access токена")
public class AccessTokenDTO {
    @Schema(description = "Токен для доступа к данным - краткосрочный",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWpuZ2xlYkBnbWFpbC5jb20iLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3NTUwMDc3NCwiZXhwIjoxNzc1NTAxNjc0fQ.-49neeI_SS8XdLoFrPidhmdQVv5ICHfLtg6fq88Jv7c"
    )
    private String accessToken;
}
