package project.transay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.transay.dto.requests.IndicatorRequestDTO;
import project.transay.dto.requests.MeasureRequestDTO;
import project.transay.dto.responses.IndicatorResponseDTO;
import project.transay.exceptions.EntityNotFoundException;
import project.transay.models.Indicator;
import project.transay.models.Person;
import project.transay.repositories.IndicatorRepository;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.transay.AssayApplication.DATE_FORMAT;
import static project.transay.models.AgeRange.daysToRange;
import static project.transay.models.ConditionEnum.GRAVID;
import static project.transay.models.GenderEnum.MALE;
import static project.transay.utils.StaticMethods.getDaysBetween;

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

    public ResponseEntity<List<IndicatorResponseDTO>> findAll() {
        return status(HttpStatus.OK).body(indicatorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList());
    }

    public Indicator findById(int id) {
        return indicatorRepository.getIndicatorById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Индикатор с id='%s' не найден!", id)));
    }

    @Transactional
    public ResponseEntity<?> create(IndicatorRequestDTO dto) {
        Indicator toValidate = convertToEntity(dto);
        String isValid = validateIndicator(toValidate);

        if (isValid.equals("ok")) {
            Indicator saved = indicatorRepository.save(toValidate);
            return ok(convertToDTO(saved));
        }
        return status(BAD_REQUEST).body(isValid);
    }

    @Transactional
    public ResponseEntity<IndicatorResponseDTO> update(int indicatorId, IndicatorRequestDTO dto) {
        Indicator prepared = findById(indicatorId);
        Indicator updated = indicatorRepository.save(convertToEntity(dto, prepared));
        return ok(convertToDTO(updated));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        indicatorRepository.deleteById(id);
        return status(NO_CONTENT).build();
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

    private String validateIndicator(Indicator toValidate) {
        if (toValidate.getMinAge() < 0) {
            return "Минимальный возраст не может быть отрицательным!";
        }
        if (toValidate.getMaxAge() < 0) {
            return "Максимальный возраст не может быть отрицательным!";
        }
        if (toValidate.getMaxAge() <= toValidate.getMinAge()) {
            return format("Максимальный возраст='%d' должен быть больше чем Минимальный возраст='%d'!", toValidate.getMaxAge(), toValidate.getMinAge());
        }
        if (toValidate.getMaxValue() <= toValidate.getMinValue()) {
            return format("Максимальное значение='%.2f' должен быть больше чем Минимальное значение='%.2f'!", toValidate.getMaxValue(), toValidate.getMinValue());
        }
        if (toValidate.getGender().equals(MALE)
                && toValidate.getCondition().equals(GRAVID)) {
            return "Индикатор для мужчины не может быть со статусом беременный!";
        }
        return "ok";
    }
}
