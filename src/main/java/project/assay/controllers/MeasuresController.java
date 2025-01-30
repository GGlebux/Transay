package project.assay.controllers;


import jakarta.validation.Valid;

import java.net.URI;
import java.util.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.assay.dto.IndicatorDTO;
import project.assay.dto.MeasureDTO;
import project.assay.dto.MeasureUpdateDTO;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.Measure;
import project.assay.models.Referent;
import project.assay.services.IndicatorService;
import project.assay.services.PeopleService;
import project.assay.services.MeasureService;
import project.assay.services.ReferentService;

/**
 *
 */
@RestController
@RequestMapping("/people/{personId}/measures")
public class MeasuresController {

    private final IndicatorService indicatorService;
    private final PeopleService peopleService;
    private final ReferentService referentService;
    private final ModelMapper modelMapper;
    private final MeasureService measureService;

    @Autowired
    public MeasuresController(IndicatorService indicatorService, PeopleService peopleService,
                              ReferentService referentService,
                              ModelMapper modelMapper, MeasureService measureService) {
        this.indicatorService = indicatorService;
        this.peopleService = peopleService;
        this.referentService = referentService;
        this.modelMapper = modelMapper;
        this.measureService = measureService;
    }

    /**
     * Отображение списка корректных индикаторов для конкретного человека
     *
     * @param personId id человека
     */
    @GetMapping("/correct")
    public ResponseEntity<List<IndicatorDTO>> showCorrectList(@PathVariable("personId") int personId) {
        Person person = peopleService.findById(personId);
        List<Indicator> indicators = indicatorService.findAllCorrect(person);
        if (indicators.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(indicators.stream().map(this::convertToIndicatorDTO).toList());
    }

    /**
     * Отображение списка референтных значений в отсортированном виде
     *
     * @param personId id человека
     */
    @GetMapping
    public Map<String, List<MeasureDTO>> showMeasures(@PathVariable("personId") int personId) {
        List<Measure> measures = measureService.findAllById(personId);
        return createSummaryTable(measures);
    }

    /**
     * Создает корректное реферетное значение для определенного человека
     *
     * @param measureUpdateDTO конкретное референтое значение
     * @param personId         id человека
     * @return URI + String
     */
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid MeasureUpdateDTO measureUpdateDTO,
                                         @PathVariable("personId") int personId) {

        Person person = peopleService.findById(personId);
        Indicator indicator = indicatorService.findById(measureUpdateDTO.getCelectedId());

// ToDo: Добавить проверку того, что сохраняется верный индикатор для человека
        Referent referent = convertToReferent(measureUpdateDTO);
        referentService.enrich(referent, indicator);
        referentService.save(referent);

        Measure measure = new Measure();
        measure.setPerson(person);
        measure.setIndicator(indicator);
        measure.setReferent(referent);
        int resultId = measureService.save(measure);

        return ResponseEntity.created(URI.create("/people/" + personId + "/measures/" + resultId))
                .body("Create measure with id=" + resultId);
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
        measureService.deleteById(measureId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private IndicatorDTO convertToIndicatorDTO(Indicator indicator) {
        return modelMapper.map(indicator, IndicatorDTO.class);
    }

    private Referent convertToReferent(MeasureUpdateDTO measureUpdateDTO) {
        return modelMapper.map(measureUpdateDTO, Referent.class);
    }

    private MeasureDTO convertToMeasureDTO(Measure measure) {
        Indicator i = measure.getIndicator();
        Referent r = measure.getReferent();
        MeasureDTO result = MeasureDTO.builder()
                .id(measure.getId())
                .minValue(i.getMinValue())
                .currentValue(r.getCurrentValue())
                .maxValue(i.getMaxValue())
                .regDate(r.getRegDate())
                .units(r.getUnits())
                .status(r.getStatus())
                .reasons(r.getReasons())
                .build();
        return result;

    }
    private Map<String, List<MeasureDTO>> createSummaryTable(List<Measure> measures) {
        Map<String, List<MeasureDTO>> summaryTable = new HashMap<>();
        for (Measure measure : measures) {
            Indicator indicator = measure.getIndicator();
            String name = indicator.getName();
            MeasureDTO measureDTO = convertToMeasureDTO(measure);
            if (summaryTable.containsKey(name)) {
                summaryTable.get(name).add(measureDTO);
            } else {
                List<MeasureDTO> measuresList = new ArrayList<>(List.of(measureDTO));
                summaryTable.put(name, measuresList);
            }

        }
        return summaryTable;
    }
}