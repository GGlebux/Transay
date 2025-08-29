package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.requests.MeasureRequestDTO;
import project.assay.dto.responses.IndicatorResponceDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;

import java.time.LocalDate;
import java.util.List;

import static java.lang.String.format;
import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.models.AgeRange.convertToRange;
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
                .createTypeMap(Indicator.class, IndicatorResponceDTO.class)
                .addMappings(mapper -> {
                    mapper.skip(IndicatorResponceDTO::setMinAge);
                    mapper.skip(IndicatorResponceDTO::setMaxAge);
                });
        modelMapper
                .createTypeMap(IndicatorResponceDTO.class, Indicator.class)
                .addMappings(mapper -> {
                    mapper.skip(Indicator::setMinAge);
                    mapper.skip(Indicator::setMaxAge);
                });
        this.indicatorRepository = indicatorRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<IndicatorResponceDTO>> findAll() {
        return status(OK).body(indicatorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList());
    }

    public Indicator findById(int id) {
        return indicatorRepository.getIndicatorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Indicator with id=" + id + " not found"));
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
    public ResponseEntity<IndicatorResponceDTO> update(int indicatorId, IndicatorRequestDTO dto) {
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
        LocalDate dateOfBirth = person.getDateOfBirth();
        LocalDate regDate = dto.getRegDate();
        if (regDate.isBefore(dateOfBirth) || regDate.isAfter(now())) {
            throw new EntityNotFoundException("'regDate' must be between 'dateOfBirth' and 'now'");
        }
        int age = getDaysBetween(dateOfBirth, regDate);
        return indicatorRepository.findAllCorrect(dto.getName(),
                dto.getUnits(),
                person.getGender(),
                person.getIsGravid(),
                age);
    }


    protected String checkValue(Indicator indicator, double value) {
        double minValue = indicator.getMinValue();
        double maxValue = indicator.getMaxValue();
        if (value < minValue) {
            return "fall";
        } else if (value > maxValue) {
            return "raise";
        } else {
            return "ok";
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

    private IndicatorResponceDTO convertToDTO(Indicator indicator) {
        IndicatorResponceDTO dto = new IndicatorResponceDTO();
        modelMapper.map(indicator, dto);
        dto.setMinAge(convertToRange(indicator.getMinAge()));
        dto.setMaxAge(convertToRange(indicator.getMaxAge()));
        return dto;
    }

    private String validateIndicator(Indicator toValidate) {
        if (toValidate.getMinAge() < 0) {
            return "'minAge' should be negative";
        }
        if (toValidate.getMaxAge() < 0) {
            return "'maxAge' should be negative";
        }
        if (toValidate.getMaxAge() <= toValidate.getMinAge()) {
            return format("maxAge '%d' should be greater than minAge '%d'", toValidate.getMaxAge(), toValidate.getMinAge());
        }
        if (toValidate.getMaxValue() <= toValidate.getMinValue()) {
            return format("maxValue '%.2f' should be greater than minValue '%.2f'", toValidate.getMaxValue(), toValidate.getMinValue());
        }
        if (toValidate.getGender().equals("male") && toValidate.isGravid()) {
            return "male gender should be gravid";
        }
        return "ok";
    }
}
