package project.assay.controllers;


import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.IndicatorDTO;
import project.assay.dto.MeasureResponceDTO;
import project.assay.dto.MeasureRequestDTO;
import project.assay.services.MeasureService;

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
     * Отображение списка корректных индикаторов для конкретного человека
     *
     * @param personId id человека
     */
    @GetMapping("/correct")
    public ResponseEntity<List<IndicatorDTO>> showCorrectList(@PathVariable("personId") int personId) {
        return measureService.findAllCorrect(personId);
    }

    /**
     * Отображение списка референтных значений в отсортированном виде
     *
     * @param personId id человека
     */
    @GetMapping
    public ResponseEntity<Map<String, List<MeasureResponceDTO>>>  showMeasures(@PathVariable("personId") int personId) {
        return measureService.createSummaryTable(personId);
    }

    @GetMapping("/decrypt")
    public ResponseEntity<Map<String, Double>> decrypt(@RequestParam("date") @Valid LocalDate date,
                                             @PathVariable("personId") int personId) {
        return measureService.getDecryptedMeasures(personId, date);
    }

    /**
     * Создает корректное реферетное значение для определенного человека
     *
     * @param measureRequestDTO конкретное референтое значение
     * @param personId         id человека
     * @return URI + String
     */
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid MeasureRequestDTO measureRequestDTO,
                                         @PathVariable("personId") int personId) {
        return measureService.save(measureRequestDTO, personId);
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