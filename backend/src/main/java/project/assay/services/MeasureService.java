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
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MeasureService(MeasureRepository measureRepository,
                          IndicatorService indicatorService,
                          ReferentService referentService,
                          ModelMapper modelMapper,
                          CustomerService customerService,
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
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public MeasureResponseDTO save(MeasureRequestDTO dto, Optional<Integer> measureId) {
        Person person = customerService.getPersonFromAuth();
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
        Person person = customerService.getPersonFromAuth();
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
        Person person = customerService.getPersonFromAuth();
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
        Person person = customerService.getPersonFromAuth();
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
