package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responces.MeasureResponceDTO;
import project.assay.dto.responces.SimpleIndicatorResponceDTO;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.round;
import static java.util.Comparator.reverseOrder;
import static java.util.List.of;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.ResponseEntity.*;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final PeopleService peopleService;
    private final IndicatorService indicatorService;
    private final ReferentService referentService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasureService(PeopleService peopleService, MeasureRepository measureRepository, IndicatorService indicatorService, ReferentService referentService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
        this.referentService = referentService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        this.modelMapper
                .createTypeMap(MeasureRequestDTO.class, Referent.class)
                .addMappings(mapper -> mapper.skip(Referent::setId));
    }

    private List<Measure> findAllByPersonId(int personId) {
        return measureRepository.findByPersonId(personId);
    }

    @Transactional
    public ResponseEntity<List<SimpleIndicatorResponceDTO>> findAllCorrect(int personId) {
        Person person = peopleService.findById(personId);
        return ok(indicatorService
                .findAllCorrect(person)
                .stream()
                .map(this::convertToSimpleDTO)
                .sorted()
                .toList());
    }

    @Transactional
    public ResponseEntity<MeasureResponceDTO> save(MeasureRequestDTO measureRequestDTO, int personId) {
        Person person = peopleService.findById(personId);

        // Есть ли такой индикатор?
        int selectedId = measureRequestDTO.getSelectedId();
        Indicator indicator = indicatorService.findById(selectedId);

        // Можно ли создать такое измерение?
        String canCreate = this.canCreateMeasure(person, measureRequestDTO, indicator);
        if (!canCreate.equals("ok")) {
            throw new EntityNotCreatedException(canCreate);
        }

        Referent referent = convertToReferent(measureRequestDTO);
        referentService.enrich(referent, indicator);
        referentService.save(referent);

        Measure measure = Measure.builder()
                .person(person)
                .indicator(indicator)
                .referent(referent).build();

        Measure saved = measureRepository.save(measure);

        return ok(convertToMeasureDTO(saved));
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteById(int measureId) {
        measureRepository.deleteById(measureId);
        return noContent().build();
    }

    public String canCreateMeasure(Person person, MeasureRequestDTO verifiableMeasure, Indicator selectedIndicator) {
        int selectedId = verifiableMeasure.getSelectedId();
        List<Indicator> indicators = indicatorService.findAllCorrect(person);

        // Проверка выбран ли корректный индикатор для конкретного человека
        Set<Integer> idCorrectIndicators = indicators
                .stream()
                .map(Indicator::getId)
                .collect(toSet());
        if (!idCorrectIndicators.contains(selectedId)) {
            return "Incorrect selection indicator id!";
        }

        // Проверка дубликатов такого индикатора на одну дату
        Set<LocalDate> referentsDates = person.getMeasureList()
                .stream()
                .filter(measure -> measure.getIndicator().getEngName().equals(selectedIndicator.getEngName()))
                .map(Measure::getReferent)
                .map(Referent::getRegDate)
                .collect(toSet());
        if (referentsDates.contains(verifiableMeasure.getRegDate())) {
            return "Selected Indicator with this date already exists!";
        }
        return "ok";
    }


    private Referent convertToReferent(MeasureRequestDTO measureRequestDTO) {
        return modelMapper.map(measureRequestDTO, Referent.class);
    }

    public ResponseEntity<Map<String, List<MeasureResponceDTO>>> createSummaryTable(int personId) {
        List<Measure> measures = this.findAllByPersonId(personId);

        Map<String, List<MeasureResponceDTO>> summaryTable = new HashMap<>();
        for (Measure measure : measures) {
            String name = measure.getIndicator().getEngName();
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
        List<Reason> excludedReasons = peopleService.findAllExReasons(personId);

        Map<String, Double> decryptedMeasures = new HashMap<>();
        int counter = 0;
        for (Measure measure : measures) {
            Referent referent = measure.getReferent();
            if (referent.getStatus().equals("ok")) {
                continue;
            }
            List<Reason> personReasons = referent.getVerdict();
            for (Reason reason : personReasons) {
                String name = reason.getName();
                if (excludedReasons.contains(reason)) {
                    continue;
                }
                if (decryptedMeasures.containsKey(name)) {
                    decryptedMeasures.merge(name, 1.0, Double::sum);
                } else {
                    decryptedMeasures.put(name, 1.0);
                }
                counter++;
            }
        }
        for (Map.Entry<String, Double> entry : decryptedMeasures.entrySet()) {
            Double newValue = round(entry.getValue() / counter * 1000.0) / 1000.0;
            entry.setValue(newValue);
        }
        Map<String, Double> result = decryptedMeasures
                .entrySet()
                .stream()
                .sorted(comparingByValue(reverseOrder()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, _) -> oldValue,
                        LinkedHashMap::new));
        return ok(result);
    }

    private SimpleIndicatorResponceDTO convertToSimpleDTO(Indicator indicator) {
        SimpleIndicatorResponceDTO dto = modelMapper.map(indicator, SimpleIndicatorResponceDTO.class);
        dto.setName(indicator.getRusName());
        return dto;
    }

    private MeasureResponceDTO convertToMeasureDTO(Measure m) {
        Indicator i = m.getIndicator();
        Referent r = m.getReferent();
        return new MeasureResponceDTO(m.getId(),
                i.getMinValue(),
                r.getCurrentValue(),
                i.getMaxValue(),
                r.getRegDate(),
                i.getUnits(),
                r.getStatus(),
                r.getVerdict());
    }
}
