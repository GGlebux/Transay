package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responces.DecryptValueDTO;
import project.assay.dto.responces.MeasureResponceDTO;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

import java.time.LocalDate;
import java.util.*;

import static java.util.Comparator.*;
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
        List<Indicator> indicators = indicatorService.findAllCorrectIndicators(person);

        // Проверка выбран ли корректный индикатор для конкретного человека
        Set<Integer> idCorrectIndicators = indicators
                .stream()
                .map(Indicator::getId)
                .collect(toSet());
        if (!idCorrectIndicators.contains(selectedId)) {
            return "Incorrect selection indicator id!";
        }

        // Проверка дубликатов такого индикатора на одну дату
        Set<LocalDate> referentsDates = person.getMeasures()
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

    public ResponseEntity<Map<String, DecryptValueDTO>> getDecryptedMeasures(int personId, LocalDate date) {
        List<Measure> measures = measureRepository.findByPersonIdAndDate(personId, date); // Измерение человека по дате
        Set<Reason> excludedReasons = peopleService.findAllExReasons(personId); // Исключенные причины человека

        Map<String, DecryptValueDTO> decryptedMeasures = createDecryptMap(measures, excludedReasons);

        // Сортируем по значению
        return ok(sortDecryptMap(decryptedMeasures));
    }

    private static Map<String, DecryptValueDTO> createDecryptMap(List<Measure> measures, Set<Reason> excludedReasons) {
        Map<String, DecryptValueDTO> decryptedMeasures = new HashMap<>();
//      Проходим по всем измерениям
        for (Measure measure : measures) {

//          Проверяем каждый референт
            Referent referent = measure.getReferent();
            if (referent.getStatus().equals("ok")) {
                continue;
            }

//          Если не в норме, то добавляем
            List<Reason> referentVerdict = referent.getVerdict();
            for (Reason reason : referentVerdict) {
                String name = reason.getName();
                // Если причина попадает в исключенные - не учитываем
                if (excludedReasons.contains(reason)) {
                    continue;
                }

                String rusIndicatorName = measure.getIndicator().getRusName();

                if (decryptedMeasures.containsKey(name)) {
                    DecryptValueDTO valueToUpdate = decryptedMeasures.get(name);
                    valueToUpdate.getIndicators().add(rusIndicatorName);
                    valueToUpdate.increment();
                    decryptedMeasures.put(name, valueToUpdate);
                } else {
                    DecryptValueDTO newValue = new DecryptValueDTO(1, new HashSet<>(Set.of(rusIndicatorName)));
                    decryptedMeasures.put(name, newValue);
                }
            }
        }
        return decryptedMeasures;
    }

    private static Map<String, DecryptValueDTO> sortDecryptMap(Map<String, DecryptValueDTO> decryptedMeasures) {
        return decryptedMeasures
                .entrySet()
                .stream()
                .sorted(comparingByValue(comparingInt(DecryptValueDTO::getMatchesCount).reversed()))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, _) -> oldValue,
                        LinkedHashMap::new));
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
