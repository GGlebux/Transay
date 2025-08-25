package project.assay.services;

import org.apache.poi.ss.formula.functions.T;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.SimpleIndicatorDTO;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.*;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.Comparator.comparingInt;
import static java.util.Map.Entry.comparingByValue;
import static java.util.Set.of;
import static java.util.stream.Collectors.*;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final PeopleService peopleService;
    private final IndicatorService indicatorService;
    private final ReferentService referentService;
    private final IndicatorGroupService groupService;
    private final ModelMapper modelMapper;
    private final TranscriptService transcriptService;
    private List<IndicatorGroupResponseDTO> indicatorGroups;


    @Autowired
    public MeasureService(PeopleService peopleService, MeasureRepository measureRepository, IndicatorService indicatorService, ReferentService referentService, IndicatorGroupService groupService, ModelMapper modelMapper, TranscriptService transcriptService) {
        this.peopleService = peopleService;
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
        this.referentService = referentService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        this.modelMapper
                .createTypeMap(MeasureRequestDTO.class, Referent.class)
                .addMappings(mapper -> mapper.skip(Referent::setId));
        this.groupService = groupService;
        this.indicatorGroups = new ArrayList<>();
        this.transcriptService = transcriptService;
    }

    private List<Measure> findAllByPersonId(int personId) {
        return measureRepository.findByPersonId(personId);
    }


    @Transactional
    public ResponseEntity<MeasureResponceDTO> save(MeasureRequestDTO dto, int personId, Optional<Integer> measureId) {
        Person person = peopleService.findById(personId);
        Measure measure = new Measure();

        // ToDo: здесь может возникать непонимание если было найдено больше 1 индикатора
        // Есть ли такой индикатор?
        List<Indicator> indicators = indicatorService.findAllCorrectIndicators(person, dto);
        Indicator indicator = indicators.getFirst();

        // Можно ли создать такое измерение?
        // Изменяем без проверки или создаем с проверкой
        measureId
                .ifPresentOrElse((id) -> {
                            validateMeasureByOwner(personId, id);
                            measure.setId(id);
                        },
                        () -> this.canCreateMeasure(person, dto, indicator));

        Referent referent = convertToReferent(dto);
        referentService.enrich(referent, indicator);
        referentService.save(referent);

        measure.setIndicator(indicator);
        measure.setPerson(person);
        measure.setReferent(referent);

        Measure saved = measureRepository.save(measure);

        return ok(convertToMeasureDTO(saved));
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteById(int measureId) {
        measureRepository.deleteById(measureId);
        return noContent().build();
    }

    /**
     * Валидирует измерение на возможность создания
     *
     * @param person            человек
     * @param verifiableMeasure измерение на проверку
     * @param indicatorDB       - индикатор из базы данных
     */
    public void canCreateMeasure(Person person, MeasureRequestDTO verifiableMeasure, Indicator indicatorDB) {
        // Проверка дубликатов такого индикатора на одну дату
        Set<LocalDate> referentsDates = person
                .getMeasures()
                .stream()
                .filter(measure -> measure
                        .getIndicator()
                        .getRusName()
                        .equals(indicatorDB
                                .getRusName()))
                .map(Measure::getReferent)
                .map(Referent::getRegDate)
                .collect(toSet());
        if (referentsDates.contains(verifiableMeasure.getRegDate())) {
            throw new EntityNotCreatedException(format("Indicator with name='%s' and date='%tF' already exists!",
                    indicatorDB.getRusName(),
                    verifiableMeasure.getRegDate()));
        }
    }


    /**
     * @param personId id человека
     * @return {@code ResponseEntity<Set<SummaryTableGroupDTO>>} - готовая сущность для таблицы.
     * Строится из методов {@link #createSetMeasuresByName(List, String)} и {@link #createSetOfTableGroups(List, Map)}
     */
    public ResponseEntity<Set<SummaryTableGroupDTO>> createSummaryTable(int personId) {
        Person person = peopleService.findById(personId);

        // Все измерения одного человека
        List<Measure> measures = this.findAllByPersonId(personId);

        // Карта
        Map<String, Set<MeasureResponceDTO>> map = createSetMeasuresByName(measures, person.getGender());

        // Важно чтобы выполнилось обновление групп индикаторов
        this.updateIndicatorGroups();

        Set<SummaryTableGroupDTO> result = createSetOfTableGroups(measures, map);

        return ok(result);
    }

    private Map<String, Transcript> getTranscriptsOptimized(List<Measure> measures, String gender) {
        Set<String> transcriptNames =
                measures
                        .stream()
                        .map(Measure::getIndicator)
                        .map(Indicator::getEngName)
                        .collect(toSet());

        return transcriptService
                .findAllByNameIn(transcriptNames, gender)
                .stream()
                .collect(toMap(Transcript::getName,
                        transcript -> transcript));
    }


    /**
     * @param measures список существующих измерений человека
     * @return {@code Map<String, Set<MeasureResponceDTO>>}
     * key-название индикатора,
     * value-множество упрощенных измерений (отсортированы по возрастанию даты)
     */
    @NotNull
    private Map<String, Set<MeasureResponceDTO>> createSetMeasuresByName(List<Measure> measures, String gender) {
        Map<String, Set<MeasureResponceDTO>> map = new HashMap<>();

        // Оптимизированный запрос для получения всех нужных транскрипций
        Map<String, Transcript> transcripts = getTranscriptsOptimized(measures, gender);

        // Заполняем карту
        for (Measure measure : measures) {
            String indicatorRusName = measure.getIndicator().getRusName();
            String indicatorEngName = measure.getIndicator().getEngName();

            Transcript transcript = transcripts.get(indicatorEngName);
            measure.getReferent().setTranscript(transcript);

            MeasureResponceDTO measureResponceDTO = convertToMeasureDTO(measure);

            // Если причина попадает в исключенные - не учитываем
            Set<Reason> exReasons = peopleService.findAllEx(measure.getPerson().getId());
            measureResponceDTO.clearExReasons(exReasons);


            if (!map.containsKey(indicatorRusName)) {
                map.put(indicatorRusName, new TreeSet<>(of(measureResponceDTO)));
            } else {
                map.get(indicatorRusName).add(measureResponceDTO);
            }
        }
        return map;
    }

    /**
     * @param measures список существующий измерений человека
     * @param map      карта из метода {@linkplain #createSetMeasuresByName}
     * @return {@code Set<SummaryTableGroupDTO>} - сущность для корректного отображения таблицы
     * (разбиение по группам индикаторов, именам индикаторов, датам)
     */
    @NotNull
    private Set<SummaryTableGroupDTO> createSetOfTableGroups(List<Measure> measures, Map<String, Set<MeasureResponceDTO>> map) {
        // Финальная сущность
        Set<SummaryTableGroupDTO> summaryTable = new TreeSet<>();
        for (IndicatorGroupResponseDTO group : this.indicatorGroups) {
            String groupName = group.getGroupName();
            Set<LocalDate> dates = this.extractDatesByGroups(measures, group.getGroupName());

            Set<TableMeta> metas = new TreeSet<>();

            for (SimpleIndicatorDTO indicator : group.getIndicators()) {
                String name = indicator.getName();
                if (map.containsKey(name)) {
                    metas.add(new TableMeta(name, map.get(name)));
                }
            }
            if (!metas.isEmpty()) {
                summaryTable.add(new SummaryTableGroupDTO(groupName, dates, metas));
            }
        }
        return summaryTable;
    }

    /**
     * @param measures  список существующий измерений человека
     * @param groupName название группы индикаторов
     * @return {@code Set<LocalDate>} - составляет список дат, когда были сданы анализы, для определенной группы индикаторов
     */
    @NotNull
    private Set<LocalDate> extractDatesByGroups(List<Measure> measures, String groupName) {
        Set<LocalDate> dates = new TreeSet<>();

        IndicatorGroupResponseDTO group = this
                .indicatorGroups
                .stream()
                .filter(dto -> dto.getGroupName().equals(groupName))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        format("IndicatorGroup with name='%s' not found!", groupName)));
        Set<String> indicatorsNamesInGroup = group
                .getIndicators()
                .stream()
                .map(SimpleIndicatorDTO::getName)
                .collect(toUnmodifiableSet());

        for (Measure measure : measures) {
            String indicatorName = measure.getIndicator().getRusName();
            if (indicatorsNamesInGroup.contains(indicatorName)) {
                dates.add(measure.getReferent().getRegDate());
            }
        }
        return dates;
    }

    /**
     * @param personId id человека
     * @param date     расшифровка составляется на эту дату
     * @return {@code ResponseEntity<Map<String, DecryptValueDTO>>}
     */
    public ResponseEntity<Map<String, DecryptValueDTO>> getDecryptedMeasures(int personId, LocalDate date) {
        List<Measure> measures = measureRepository.findByPersonIdAndDate(personId, date); // Измерение человека по дате
        Set<Reason> excludedReasons = peopleService.findAllEx(personId); // Исключенные причины человека

        Map<String, DecryptValueDTO> decryptedMeasures = createDecryptMap(measures, excludedReasons);

        // Сортируем по значению
        return ok(sortDecryptMap(decryptedMeasures));
    }

    /**
     * @param measures        список существующий измерений человека
     * @param excludedReasons - список исключенных причин у человека (например: не курит)
     * @return {@code Map<String, DecryptValueDTO>} key - название причины, value - количество повторений причины + названия индикаторов в которых имеется
     */
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
            Set<Reason> referentVerdict = referent.getVerdict();
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
                    DecryptValueDTO newValue = new DecryptValueDTO(1, new HashSet<>(of(rusIndicatorName)));
                    decryptedMeasures.put(name, newValue);
                }
            }
        }
        return decryptedMeasures;
    }

    /**
     * Обновляет локальный список групп индикаторов
     */
    private void updateIndicatorGroups() {
        this.indicatorGroups = groupService.findAll();
    }

    /**
     * @param decryptedMeasures карты из метода {@link #createDecryptMap(List, Set)}
     * @return {@code Map<String, DecryptValueDTO>} - сортированная карта по количествам совпадений
     */
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

    private void validateMeasureByOwner(int personId, int measureId) {
        Set<Integer> personMeasureIds = measureRepository
                .findByPersonId(personId)
                .stream()
                .map(Measure::getId)
                .collect(toSet());
        if (!personMeasureIds.contains(measureId)) {
            throw new EntityNotFoundException(
                    format("Measure with id='%d' does not belong to person with id='%d'!", measureId, personId));
        }
    }

    private Referent convertToReferent(MeasureRequestDTO measureRequestDTO) {
        return modelMapper.map(measureRequestDTO, Referent.class);
    }

    private static MeasureResponceDTO convertToMeasureDTO(Measure m) {
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

    private static MeasureResponceDTO convertToMeasureDTO(Measure m, Transcript transcript) {
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
