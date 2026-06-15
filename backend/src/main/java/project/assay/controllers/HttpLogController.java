package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.responses.HttpLogDetailDTO;
import project.assay.dto.responses.HttpLogSummaryDTO;
import project.assay.services.HttpLogService;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static project.assay.models.Roles.ADMIN;

@RestController
@RequestMapping("/api/admin/http-logs")
@Tag(name = "Технический мониторинг", description = "Аудит HTTP-запросов (только ADMIN)")
@PreAuthorize(ADMIN)
public class HttpLogController {

    private final HttpLogService httpLogService;

    public HttpLogController(HttpLogService httpLogService) {
        this.httpLogService = httpLogService;
    }

    @GetMapping
    @Operation(summary = "Лента запросов с пагинацией и фильтром по ошибкам (5xx)")
    public ResponseEntity<Page<HttpLogSummaryDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "false") boolean onlyErrors) {
        return ok(httpLogService.find(onlyErrors, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Полный запрос/ответ по id (с телами)")
    public ResponseEntity<HttpLogDetailDTO> get(@PathVariable Long id) {
        return ok(httpLogService.getById(id));
    }

    @DeleteMapping
    @Operation(summary = "Очистить весь лог")
    public ResponseEntity<Void> clear() {
        httpLogService.clear();
        return noContent().build();
    }
}
