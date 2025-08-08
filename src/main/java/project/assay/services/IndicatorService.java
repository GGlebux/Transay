package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.responces.IndicatorResponceDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.models.AgeRange.convertToRange;
import static project.assay.utils.StaticMethods.*;

@Service
@Transactional(readOnly = true)
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;
    private final ModelMapper modelMapper;
    private static final Set<String> UNITS;

    static {
        UNITS = Set.of("г/л",
                "тера/л",
                "фл",
                "пг",
                "%",
                "*10^(9)/л",
                "мм/час",
                "мкг/л",
                "мкмоль/л",
                "мкмоль/литр",
                "нг/мл",
                "мг/л",
                "ммоль/л",
                "МкЕд/мл",
                "ме/л",
                "ед/л",
                "мМЕ/л",
                "нг/дл",
                "пмоль/л",
                "пг/мл",
                "МЕ/мл",
                "нмоль/л",
                "мкг/сутки",
                "мкг/дл",
                "мкмоль/сут",
                "мкг/сут",
                "мкг/литр",
                "мм/ч",
                "клеток/мкл",
                "мкЕд/л");
    }

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

    public ResponseEntity<Set<String>> findAllUnits() {
        return ok(UNITS);
    }

    public List<Indicator> findAllCorrect(Person person) {
        int age = getDaysOfAge(person.getDateOfBirth());
        return indicatorRepository.findAllCorrect(person.getGender(), person.getIsGravid(), age);
    }

    @Transactional
    public ResponseEntity<?> create(IndicatorRequestDTO dto) {
        Indicator valid = convertToEntity(dto);
        if (valid.getMaxAge() <= valid.getMinAge()) {
            return status(BAD_REQUEST).body(format("maxAge '%d' should be greater than '%d'", valid.getMaxAge(), valid.getMinAge()));
        }
        Indicator saved = indicatorRepository.save(valid);
        return ok(convertToDTO(saved));
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


    public String checkValue(Indicator indicator, double value) {
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
}
