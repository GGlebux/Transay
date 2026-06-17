package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.*;
import project.assay.exceptions.DatabaseException;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

import java.time.LocalDate;
import java.util.*;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;
import static project.assay.AssayApplication.DATE_FORMAT;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final IndicatorService indicatorService;
    private final ReferentService referentService;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;
    private final PeopleService peopleService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MeasureService(MeasureRepository measureRepository,
                          IndicatorService indicatorService,
                          ReferentService referentService,
                          ModelMapper modelMapper,
                          CustomerService customerService,
                          PeopleService peopleService,
                          JdbcTemplate jdbcTemplate) {
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
        this.referentService = referentService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
        this.modelMapper
                .createTypeMap(MeasureRequestDTO.class, Referent.class)
                .addMappings(mapper -> mapper.skip(Referent::setId));
        this.customerService = customerService;
        this.peopleService = peopleService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public MeasureResponseDTO save(MeasureRequestDTO dto, Optional<Integer> measureId) {
        return doSave(customerService.getPersonFromAuth(), dto, measureId);
    }

    @Transactional
    public MeasureResponseDTO saveForPerson(long personId, MeasureRequestDTO dto, Optional<Integer> measureId) {
        return doSave(peopleService.getOwnedPerson(personId), dto, measureId);
    }

    private MeasureResponseDTO doSave(Person person, MeasureRequestDTO dto, Optional<Integer> measureId) {
        Measure measure = new Measure();

        // ToDo: здесь может возникать непонимание если было найдено больше 1 индикатора
        // Есть ли такой индикатор?
        List<Indicator> indicators = indicatorService.findAllCorrectIndicators(person, dto);

        if (indicators.isEmpty()) {
            throw new EntityNotFoundException(
                    format("Индикатор: {имя='%s',\nединицы измерения='%s'} \nдля вас: {%s} не найден!",
                            dto.getName(),
                            dto.getUnits(),
                            person));
        }

        if (indicators.size() > 1) {
            throw new EntityNotCreatedException(
                    format("Найдено '%d' корректных индикаторов: %n%s.\nСистема не может выбрать правильный!",
                            indicators.size(),
                            indicators));
        }
        Indicator indicator = indicators.getFirst();

        // Можно ли создать такое измерение?
        // Изменяем без проверки или создаем с проверкой
        measureId
                .ifPresentOrElse((id) -> {
                            validateMeasureByOwner(person.getId(), id);
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

        return convertToMeasureDTO(saved);
    }

    @Transactional
    public void deleteById(int measureId) {
        deleteForPerson(customerService.getPersonFromAuth(), measureId);
    }

    @Transactional
    public void deleteByIdForPerson(long personId, int measureId) {
        deleteForPerson(peopleService.getOwnedPerson(personId), measureId);
    }

    private void deleteForPerson(Person person, int measureId) {
        validateMeasureByOwner(person.getId(), measureId);
        measureRepository.deleteById(measureId);
    }

    /**
     * Валидирует измерение на возможность создания
     *
     * @param person            человек
     * @param verifiableMeasure измерение на проверку
     * @param indicatorDB       - индикатор из базы данных
     */
    private void canCreateMeasure(Person person, MeasureRequestDTO verifiableMeasure, Indicator indicatorDB) {
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
            throw new EntityNotCreatedException(format("Измерение с именем='%s' и датой='%s' уже создано!",
                    indicatorDB.getRusName(),
                    DATE_FORMAT.format(verifiableMeasure.getRegDate())));
        }
    }

    /**
     * Сводная таблица измерений человека. Вся логика (подбор групп, статусы, причины,
     * сортировки) — в SQL-функции get_summary_table(person_id), которая сразу возвращает
     * готовый JSON в формате {@code Set<SummaryTableGroupDTO>}. Java только проверяет
     * авторизацию и отдаёт JSON клиенту без промежуточных преобразований.
     *
     * @return готовый JSON сводной таблицы
     */
    public String createSummaryTable() {
        return summaryTableFor(customerService.getPersonFromAuth());
    }

    public String createSummaryTable(long personId) {
        return summaryTableFor(peopleService.getOwnedPerson(personId));
    }

    private String summaryTableFor(Person person) {
        return querySingleJson(
                "SELECT get_summary_table(?)::text",
                "Не удалось построить сводную таблицу анализов",
                person.getId());
    }

    /**
     * Расшифровка (топ-причины) на дату. Вся логика — в SQL-функции
     * get_decrypt(person_id, reg_date), которая сразу возвращает готовый JSON в формате
     * {@code Map<имя причины, DecryptValueDTO>} с порядком ключей по matchesCount DESC.
     *
     * @param date расшифровка составляется на эту дату
     * @return готовый JSON расшифровки
     */
    public String getDecryptedMeasures(LocalDate date) {
        return decryptFor(customerService.getPersonFromAuth(), date);
    }

    public String getDecryptedMeasures(long personId, LocalDate date) {
        return decryptFor(peopleService.getOwnedPerson(personId), date);
    }

    private String decryptFor(Person person, LocalDate date) {
        return querySingleJson(
                "SELECT get_decrypt(?, ?)::text",
                "Не удалось получить расшифровку анализов",
                person.getId(), date);
    }

    /**
     * Выполняет SQL-функцию, возвращающую один JSON-столбец (text), и пробрасывает
     * сбои БД клиенту в едином формате ProblemDetail.
     */
    private String querySingleJson(String sql, String errorMessage, Object... args) {
        try {
            return jdbcTemplate.queryForObject(sql, String.class, args);
        } catch (DataAccessException e) {
            throw new DatabaseException(errorMessage, e);
        }
    }

    private void validateMeasureByOwner(Long personId, int measureId) {
        Set<Integer> personMeasureIds = measureRepository
                .findByPersonId(personId)
                .stream()
                .map(Measure::getId)
                .collect(toSet());
        if (!personMeasureIds.contains(measureId)) {
            throw new EntityNotFoundException(
                    format("Измерение с id='%d' не принадлежит человеку с id='%d'!", measureId, personId));
        }
    }

    private Referent convertToReferent(MeasureRequestDTO measureRequestDTO) {
        return modelMapper.map(measureRequestDTO, Referent.class);
    }

    private static MeasureResponseDTO convertToMeasureDTO(Measure m) {
        Indicator i = m.getIndicator();
        Referent r = m.getReferent();
        return new MeasureResponseDTO(m.getId(),
                i.getMinValue(),
                r.getCurrentValue(),
                i.getMaxValue(),
                r.getRegDate(),
                i.getUnits(),
                r.getStatus(),
                r.getVerdict());
    }
}
