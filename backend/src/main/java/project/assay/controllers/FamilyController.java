package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.responses.MeasureResponseDTO;
import project.assay.dto.responses.PersonResponseDTO;
import project.assay.models.Reason;
import project.assay.services.ExReasonService;
import project.assay.services.MeasureService;
import project.assay.services.PeopleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.net.URI.create;
import static java.util.Optional.empty;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.*;
import static project.assay.models.Roles.USER;

/**
 * Семья: люди, привязанные к аккаунту (кроме собственной анкеты пользователя).
 * Для каждого члена семьи доступны те же операции, что и в личном разделе:
 * CRUD анкеты, сводная таблица измерений, расшифровка и исключённые причины.
 */
@RestController
@RequestMapping("/api/people/family")
@PreAuthorize(USER)
@Tag(name = "Семья - люди аккаунта")
public class FamilyController {

    private final PeopleService peopleService;
    private final MeasureService measureService;
    private final ExReasonService exReasonService;

    @Autowired
    public FamilyController(PeopleService peopleService,
                            MeasureService measureService,
                            ExReasonService exReasonService) {
        this.peopleService = peopleService;
        this.measureService = measureService;
        this.exReasonService = exReasonService;
    }

    /* ---------- Анкеты членов семьи ---------- */

    @GetMapping
    @Operation(summary = "Список членов семьи")
    public ResponseEntity<List<PersonResponseDTO>> list() {
        return ok(peopleService.findFamily());
    }

    @PostMapping
    @Operation(summary = "Добавить члена семьи")
    public ResponseEntity<PersonResponseDTO> createMember(@RequestBody @Valid PersonRequestDTO dto) {
        var response = peopleService.createFamilyMember(dto);
        return created(create("/api/people/family/" + response.getId())).body(response);
    }

    @GetMapping("/{personId}")
    @Operation(summary = "Данные члена семьи")
    public ResponseEntity<PersonResponseDTO> get(@PathVariable long personId) {
        return ok(peopleService.findFamilyMember(personId));
    }

    @PatchMapping("/{personId}")
    @Operation(summary = "Обновить члена семьи")
    public ResponseEntity<PersonResponseDTO> update(@PathVariable long personId,
                                                    @RequestBody @Valid PersonRequestDTO dto) {
        return ok(peopleService.updateFamilyMember(personId, dto));
    }

    @DeleteMapping("/{personId}")
    @Operation(summary = "Удалить члена семьи")
    public ResponseEntity<HttpStatus> delete(@PathVariable long personId) {
        peopleService.deleteFamilyMember(personId);
        return noContent().build();
    }

    /* ---------- Измерения члена семьи ---------- */

    @GetMapping(value = "/{personId}/measures", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Сводная таблица члена семьи")
    public ResponseEntity<String> summary(@PathVariable long personId) {
        return ok().contentType(APPLICATION_JSON).body(measureService.createSummaryTable(personId));
    }

    @GetMapping(value = "/{personId}/measures/decrypt", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Расшифровка члена семьи на дату")
    public ResponseEntity<String> decrypt(@PathVariable long personId,
                                          @RequestParam("date") @Valid LocalDate date) {
        return ok().contentType(APPLICATION_JSON).body(measureService.getDecryptedMeasures(personId, date));
    }

    @PostMapping("/{personId}/measures")
    @Operation(summary = "Создать измерение члену семьи")
    public ResponseEntity<MeasureResponseDTO> createMeasure(@PathVariable long personId,
                                                            @RequestBody @Valid MeasureRequestDTO dto) {
        var response = measureService.saveForPerson(personId, dto, empty());
        return created(create("/api/people/family/" + personId + "/measures/" + response.getId())).body(response);
    }

    @PatchMapping("/{personId}/measures/{measureId}")
    @Operation(summary = "Обновить измерение члена семьи")
    public ResponseEntity<MeasureResponseDTO> updateMeasure(@PathVariable long personId,
                                                            @PathVariable Optional<Integer> measureId,
                                                            @RequestBody @Valid MeasureRequestDTO dto) {
        return ok(measureService.saveForPerson(personId, dto, measureId));
    }

    @DeleteMapping("/{personId}/measures/{measureId}")
    @Operation(summary = "Удалить измерение члена семьи")
    public ResponseEntity<HttpStatus> deleteMeasure(@PathVariable long personId,
                                                    @PathVariable int measureId) {
        measureService.deleteByIdForPerson(personId, measureId);
        return noContent().build();
    }

    /* ---------- Исключённые причины члена семьи ---------- */

    @GetMapping("/{personId}/ex_reasons")
    @Operation(summary = "Исключённые причины члена семьи")
    public ResponseEntity<List<Reason>> exList(@PathVariable long personId) {
        return ok(exReasonService.findAllExForPerson(personId));
    }

    @PostMapping("/{personId}/ex_reasons")
    @Operation(summary = "Добавить исключённые причины члену семьи")
    public ResponseEntity<List<Reason>> exAdd(@PathVariable long personId,
                                              @RequestBody Set<Integer> reasons) {
        return ok(exReasonService.addExcludedReasonsForPerson(personId, reasons));
    }

    @DeleteMapping("/{personId}/ex_reasons/{reasonId}")
    @Operation(summary = "Удалить исключённую причину у члена семьи")
    public ResponseEntity<HttpStatus> exDelete(@PathVariable long personId,
                                               @PathVariable int reasonId) {
        exReasonService.deleteExForPerson(personId, reasonId);
        return noContent().build();
    }
}
