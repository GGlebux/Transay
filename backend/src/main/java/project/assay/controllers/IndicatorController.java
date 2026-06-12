package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.responses.IndicatorResponseDTO;
import project.assay.services.IndicatorService;

import java.util.List;

import static java.net.URI.create;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.EDITOR;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/indicators")
@PreAuthorize(EDITOR)
@Tag(name = "Индикаторы")
public class IndicatorController {
    private final IndicatorService indicatorService;

    @Autowired
    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping
    @PreAuthorize(USER)
    @Operation(summary = "Получить все индикаторы")
    public ResponseEntity<List<IndicatorResponseDTO>> getAllIndicators() {
        return ok(indicatorService.findAll());
    }

    @PostMapping
    @Operation(summary = "Создать индикатор")
    public ResponseEntity<IndicatorResponseDTO> createIndicator(@RequestBody IndicatorRequestDTO dto) {
        var response = indicatorService.create(dto);
        var location = create("/api/indicators/" + response.getId());
        return  created(location).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить индикатор")
    public ResponseEntity<IndicatorResponseDTO> updateIndicator(@PathVariable("id") int indicatorId,
                                                                @RequestBody IndicatorRequestDTO dto) {
        return ok(indicatorService.update(indicatorId, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить индикатор")
    public ResponseEntity<HttpStatus> deleteIndicator(@PathVariable int id) {
        indicatorService.delete(id);
        return noContent().build();
    }
}
