package project.transay.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.transay.dto.requests.MeasureRequestDTO;
import project.transay.dto.responses.DecryptReasonDTO;
import project.transay.dto.responses.MeasureResponseDTO;
import project.transay.dto.responses.SummaryTableGroupDTO;
import project.transay.services.MeasureService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;

/**
 *
 */
@RestController
@RequestMapping("/people/{personId}/measures")
public class MeasuresController {


    private final MeasureService measureService;

    @Autowired
    public MeasuresController(MeasureService measureService) {
        this.measureService = measureService;
    }

    /**
     * Отображение списка референтных значений в отсортированном виде
     *
     * @param personId id человека
     */
    @GetMapping
    public ResponseEntity<Set<SummaryTableGroupDTO>> summaryTable(@PathVariable("personId") int personId) {
        return measureService.createSummaryTable(personId);
    }

    @GetMapping("/decrypt")
    public ResponseEntity<Map<String, DecryptReasonDTO>> decrypt(@RequestParam("date") @Valid LocalDate date,
                                                                 @PathVariable("personId") int personId) {
        return measureService.getDecryptedMeasures(personId, date);
    }

    /**
     * Создает корректное референтное значение для определенного человека
     *
     * @param measureRequestDTO конкретное референтое значение
     * @param personId         id человека
     * @return URI + String
     */
    @PostMapping
    public ResponseEntity<MeasureResponseDTO> create(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
                                                     @PathVariable("personId") int personId) {
        return measureService.save(measureRequestDTO, personId, empty());
    }

    /**
     * Создает корректное референтное значение для определенного человека
     *
     * @param measureRequestDTO конкретное референтое значение
     * @param personId         id человека
     * @return URI + String
     */
    @PatchMapping("/{measureId}")
    public ResponseEntity<MeasureResponseDTO> update(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
                                                     @PathVariable("personId") int personId,
                                                     @PathVariable(value = "measureId") Optional<Integer> measureId) {
        return measureService.save(measureRequestDTO, personId, measureId);
    }

    /**
     * Удаляет референтное значение и measure
     *
     * @param measureId id Measure
     * @param personId  id Человека
     */
    @DeleteMapping("/{measureId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("measureId") int measureId,
                                             @PathVariable int personId) {
        return measureService.deleteById(personId, measureId);
    }


}