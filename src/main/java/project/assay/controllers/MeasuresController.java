package project.assay.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responces.DecryptValueDTO;
import project.assay.dto.responces.MeasureResponceDTO;
import project.assay.dto.responces.SimpleIndicatorResponceDTO;
import project.assay.services.IndicatorService;
import project.assay.services.MeasureService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
@RestController
@RequestMapping("/people/{personId}/measures")
public class MeasuresController {


    private final MeasureService measureService;
    private final IndicatorService indicatorService;

    @Autowired
    public MeasuresController(MeasureService measureService, IndicatorService indicatorService) {
        this.measureService = measureService;
        this.indicatorService = indicatorService;
    }

    /**
     * Отображение списка корректных индикаторов для конкретного человека
     *
     * @param personId id человека
     */
    @GetMapping("/correct")
    public ResponseEntity<Map<String, SimpleIndicatorResponceDTO>> showCorrectList(@PathVariable("personId") int personId,
                                                                            @RequestParam("group") Optional<String> group) {
        return indicatorService.getSimpleDTOByGroups(personId);
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