package project.assay.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.DecryptValueDTO;
import project.assay.dto.responses.MeasureResponceDTO;
import project.assay.dto.responses.SummaryTableGroupDTO;
import project.assay.services.MeasureService;

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
    public ResponseEntity<Map<String, DecryptValueDTO>> decrypt(@RequestParam("date") @Valid LocalDate date,
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
    public ResponseEntity<MeasureResponceDTO> create(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
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
    public ResponseEntity<MeasureResponceDTO> update(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
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
                                             @PathVariable String personId) {
        return measureService.deleteById(measureId);
    }


}