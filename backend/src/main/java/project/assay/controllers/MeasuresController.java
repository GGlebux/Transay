package project.assay.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.DecryptValueDTO;
import project.assay.dto.responses.MeasureResponseDTO;
import project.assay.dto.responses.SummaryTableGroupDTO;
import project.assay.services.MeasureService;

import java.time.LocalDate;
import java.util.Optional;

import static java.net.URI.create;
import static java.util.Optional.empty;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.USER;


@RestController
@RequestMapping("/api/people/measures")
@PreAuthorize(USER)
@Tag(name = "Измерения")
public class MeasuresController {
    private final MeasureService measureService;

    @Autowired
    public MeasuresController(MeasureService measureService) {
        this.measureService = measureService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получить всю сводную таблицу")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SummaryTableGroupDTO.class))))
    public ResponseEntity<String> summaryTable() {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(measureService.createSummaryTable());
    }

    @GetMapping(value = "/decrypt", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Получить расшифровку (часто встречаемые причины)")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = DecryptValueDTO.class)))
    public ResponseEntity<String> decrypt(@RequestParam("date") @Valid LocalDate date) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(measureService.getDecryptedMeasures(date));
    }


    @PostMapping
    @Operation(summary = "Создать измерение")
    public ResponseEntity<MeasureResponseDTO> createMeasure(@RequestBody @Valid MeasureRequestDTO measureRequestDTO) {
        var response = measureService.save(measureRequestDTO, empty());
        var uri = create("/api/people/measures/" + response.getId());
        return created(uri).body(response);
    }


    @PatchMapping("/{measureId}")
    @Operation(summary = "Обновить измерение")
    public ResponseEntity<MeasureResponseDTO> updateMeasure(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
                                                     @PathVariable(value = "measureId") Optional<Integer> measureId) {
        return ok(measureService.save(measureRequestDTO, measureId));
    }

    @DeleteMapping("/{measureId}")
    @Operation(summary = "Удалить измерение")
    public ResponseEntity<HttpStatus> deleteMeasure(@PathVariable("measureId") int measureId) {
        measureService.deleteById(measureId);
        return noContent().build();
    }
}