package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.IndicatorDTO;
import project.assay.dto.MeasureRequestDTO;
import project.assay.dto.MeasureResponceDTO;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.net.URI.create;
import static java.util.List.of;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.*;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final PeopleService peopleService;
    private final IndicatorService indicatorService;
    private final ReasonsService reasonsService;
    private final ReferentService referentService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasureService(PeopleService peopleService, MeasureRepository measureRepository, IndicatorService indicatorService, ReasonsService reasonsService, ReferentService referentService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
        this.reasonsService = reasonsService;
        this.referentService = referentService;
        this.modelMapper = modelMapper;
    }

    private List<Measure> findAllByPersonId(int personId) {
        return measureRepository.findByPersonId(personId);
    }

    @Transactional
    public ResponseEntity<List<IndicatorDTO>> findAllCorrect(int personId) {
        Person person = peopleService.find(personId);
        List<Indicator> indicators = indicatorService.findAllCorrect(person);
        if (indicators.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return ok(indicators
                .stream()
                .map(this::convertToIndicatorDTO)
                .sorted()
                .toList());
    }

    @Transactional
    public ResponseEntity<String> save(MeasureRequestDTO measureRequestDTO, int personId) {
        Person person = peopleService.find(personId);

        // Есть ли такой индикатор?
        int selectedId = measureRequestDTO.getSelectedId();
        Indicator indicator = indicatorService.findById(selectedId);

        // Можно ли создать такое измерение?
        String canCreate = this.canCreateMeasure(person, measureRequestDTO, indicator);
        if (!canCreate.equals("ok")) {
            return status(BAD_REQUEST).body(canCreate);
        }

        Referent referent = convertToReferent(measureRequestDTO);
        referentService.enrich(referent, indicator);
        referentService.save(referent);

        Measure measure = Measure.builder()
                .person(person)
                .indicator(indicator)
                .referent(referent).build();

        int resultId = measureRepository.save(measure).getId();

        return created(create("/people/" + personId + "/measures/" + resultId))
                .body("Create measure with id=" + resultId);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteById(int measureId) {
        if (!measureRepository.existsById(measureId)) {
            throw new NoSuchElementException("Measure with id=" + measureId + " not found.");
        }
        measureRepository.deleteById(measureId);
        return noContent().build();
    }

    public String canCreateMeasure(Person person, MeasureRequestDTO verifiableMeasure, Indicator selectedIndicator) {
        int selectedId = verifiableMeasure.getSelectedId();
        List<Indicator> indicators = indicatorService.findAllCorrect(person);

        Set<Integer> idCorrectIndicators = indicators
                .stream()
                .map(Indicator::getId)
                .collect(Collectors.toSet());
        if (!idCorrectIndicators.contains(selectedId)) {
            return "Incorrect selection indicator id!";
        }

        Set<LocalDate> referentsDates = person.getMeasureList()
                .stream()
                .filter(measure -> measure.getIndicator().getEng_name().equals(selectedIndicator.getEng_name()))
                .map(Measure::getReferent)
                .map(Referent::getRegDate)
                .collect(Collectors.toSet());
        if (referentsDates.contains(verifiableMeasure.getRegDate())) {
            return "Selected Indicator with this date already exists!";
        }
        return "ok";
    }

    private IndicatorDTO convertToIndicatorDTO(Indicator indicator) {
        return modelMapper.map(indicator, IndicatorDTO.class);
    }

    private Referent convertToReferent(MeasureRequestDTO measureRequestDTO) {
        return modelMapper.map(measureRequestDTO, Referent.class);
    }

    private MeasureResponceDTO convertToMeasureDTO(Measure measure) {
        Indicator i = measure.getIndicator();
        Referent r = measure.getReferent();
        return MeasureResponceDTO.builder()
                .id(measure.getId())
                .minValue(i.getMinValue())
                .currentValue(r.getCurrentValue())
                .maxValue(i.getMaxValue())
                .regDate(r.getRegDate())
                .status(r.getStatus())
                .reasons(r.getReasons())
                .build();
    }

    public ResponseEntity<Map<String, List<MeasureResponceDTO>>> createSummaryTable(int personId) {
        List<Measure> measures = this.findAllByPersonId(personId);

        Map<String, List<MeasureResponceDTO>> summaryTable = new HashMap<>();
        for (Measure measure : measures) {
            String name = measure.getIndicator().getEng_name();
            MeasureResponceDTO measureResponceDTO = convertToMeasureDTO(measure);
            if (summaryTable.containsKey(name)) {
                summaryTable.get(name).add(measureResponceDTO);
            } else {
                List<MeasureResponceDTO> measuresList = of(measureResponceDTO);
                summaryTable.put(name, measuresList);
            }
        }
        return ok(summaryTable);
    }

    public ResponseEntity<Map<String, Double>> getDecryptedMeasures(int personId, LocalDate date) {
        List<Measure> measures = measureRepository.findByPersonIdAndDate(personId, date);
        List<String> excludedReasons = reasonsService.findAll(personId)
                .stream()
                .map(ExcludedReason::getReason).toList();

        Map<String, Double> decryptedMeasures = new HashMap<>();
        int counter = 0;
        for (Measure measure : measures) {
            Referent referent = measure.getReferent();
            if (referent.getStatus().equals("ok")) {
                continue;
            }
            List<String> personReasons = referent.getReasons();
            for (String reason : personReasons) {
                if (excludedReasons.contains(reason)) {
                    continue;
                }
                if (decryptedMeasures.containsKey(reason)) {
                    decryptedMeasures.merge(reason, 1.0, Double::sum);
                } else {
                    decryptedMeasures.put(reason, 1.0);
                }
                counter++;
            }
        }
        for (Map.Entry<String, Double> entry : decryptedMeasures.entrySet()) {
            Double newValue = Math.round(entry.getValue() / counter * 1000.0) / 1000.0;
            entry.setValue(newValue);
        }
        Map<String,Double> result = decryptedMeasures
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, _) -> oldValue,
                        LinkedHashMap::new));
        return ok(result);
    }
}
