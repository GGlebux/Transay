package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.IndicatorResponseDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.exceptions.ValidationException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.enums.ReferentStatus;
import project.assay.repositories.IndicatorRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static project.assay.AssayApplication.DATE_FORMAT;
import static project.assay.models.AgeRange.daysToRange;
import static project.assay.models.enums.Condition.BASE;
import static project.assay.models.enums.IndicatorGender.MALE;
import static project.assay.models.enums.ReferentStatus.*;
import static project.assay.utils.StaticMethods.getDaysBetween;

@Service
@Transactional(readOnly = true)
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IndicatorService(IndicatorRepository indicatorRepository, ModelMapper modelMapper) {
        modelMapper
                .createTypeMap(IndicatorRequestDTO.class, Indicator.class)
                .addMappings(mapper -> {
                    mapper.skip(Indicator::setMaxAge);
                    mapper.skip(Indicator::setMinAge);
                });
        modelMapper
                .createTypeMap(Indicator.class, IndicatorResponseDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(IndicatorResponseDTO::setMinAge);
                    mapper.skip(IndicatorResponseDTO::setMaxAge);
                });
        modelMapper
                .createTypeMap(IndicatorResponseDTO.class, Indicator.class)
                .addMappings(mapper -> {
                    mapper.skip(Indicator::setMinAge);
                    mapper.skip(Indicator::setMaxAge);
                });
        this.indicatorRepository = indicatorRepository;
        this.modelMapper = modelMapper;
    }

    public List<IndicatorResponseDTO> findAll() {
        return indicatorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Indicator findById(int id) {
        return indicatorRepository.getIndicatorById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Индикатор с id='%s' не найден!", id)));
    }

    @Transactional
    public IndicatorResponseDTO create(IndicatorRequestDTO dto) {
        Indicator toValidate = convertToEntity(dto);
        validateIndicator(toValidate);

        Indicator saved = indicatorRepository.save(toValidate);
        return convertToDTO(saved);
    }

    @Transactional
    public IndicatorResponseDTO update(int indicatorId, IndicatorRequestDTO dto) {
        Indicator prepared = findById(indicatorId);
        Indicator updated = indicatorRepository.save(convertToEntity(dto, prepared));
        return convertToDTO(updated);
    }

    @Transactional
    public void delete(int id) {
        indicatorRepository.deleteById(id);
    }

    protected List<Indicator> findAllCorrectIndicators(Person person, MeasureRequestDTO dto) {
        LocalDate regDate = dto.getRegDate();
        LocalDate dateOfBirth = person.getDateOfBirth();
        LocalDate now = now();
        if (regDate.isBefore(dateOfBirth) || regDate.isAfter(now)) {
            throw new EntityNotFoundException(
                    format("'Дата анализа (%s)' должна быть между 'датой рождения (%s)' и 'текущей датой (%s)'!",
                            DATE_FORMAT.format(regDate),
                            DATE_FORMAT.format(dateOfBirth),
                            DATE_FORMAT.format(now)));
        }
        int age = getDaysBetween(dateOfBirth, regDate);
        return indicatorRepository.findAllCorrect(dto.getName(),
                dto.getUnits(),
                person.getGender(),
                person.getCondition(),
                age);
    }


    protected ReferentStatus checkValue(Indicator indicator, double value) {
        double minValue = indicator.getMinValue();
        double maxValue = indicator.getMaxValue();
        if (value < minValue) {
            return FALL;
        } else if (value > maxValue) {
            return RAISE;
        } else {
            return OK;
        }
    }

    private Indicator convertToEntity(IndicatorRequestDTO dto) {
        Indicator indicator = new Indicator();
        modelMapper.map(dto, indicator);
        indicator.setMinAge(dto.getMinAge().calculateTotalDays(false));
        indicator.setMaxAge(dto.getMaxAge().calculateTotalDays(true));
        return indicator;
    }

    private Indicator convertToEntity(IndicatorRequestDTO dto, Indicator prepared) {
        modelMapper.map(dto, prepared);
        return prepared;
    }

    private IndicatorResponseDTO convertToDTO(Indicator indicator) {
        IndicatorResponseDTO dto = new IndicatorResponseDTO();
        modelMapper.map(indicator, dto);
        dto.setMinAge(daysToRange(indicator.getMinAge(), false));
        dto.setMaxAge(daysToRange(indicator.getMaxAge(), true));
        return dto;
    }

    private void validateIndicator(Indicator toValidate) {
        var errors = new ArrayList<>(5);
        if (toValidate.getMinAge() < 0) {
            errors.add("Минимальный_возраст не может быть отрицательным!");
        }
        if (toValidate.getMaxAge() < 0) {
            errors.add("Максимальный_возраст не может быть отрицательным!");
        }
        if (toValidate.getMaxAge() <= toValidate.getMinAge()) {
            errors.add(format(
                    "Максимальный_возраст='%d' должен быть больше чем Минимальный_возраст='%d'!",
                        toValidate.getMaxAge(),
                        toValidate.getMinAge()));
        }
        if (toValidate.getMaxValue() <= toValidate.getMinValue()) {
            errors.add(format(
                    "Максимальное_значение='%.2f' должен быть больше чем Минимальное_значение='%.2f'!",
                        toValidate.getMaxValue(),
                        toValidate.getMinValue()));
        }
        if (toValidate.getGender().equals(MALE)
                && !toValidate.getCondition().equals(BASE)) {
            errors.add("Индикатор для мужчины не может быть со статусом беременный!");
        }

        if (!errors.isEmpty()) {
            var exception = new ValidationException("Ошибка при создании индикатора!");
            exception.addProperty("errors", errors);
            throw exception;
        }
    }
}
