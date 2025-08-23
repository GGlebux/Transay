package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.IndicatorRequestDTO;
import project.assay.dto.responces.IndicatorResponceDTO;
import project.assay.dto.responces.SimpleIndicatorResponceDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.IndicatorGroup;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Comparator.comparingInt;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.models.AgeRange.convertToRange;
import static project.assay.utils.StaticMethods.*;

@Service
@Transactional(readOnly = true)
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;
    private final PeopleService peopleService;
    private final IndicatorGroupService groupService;
    private final ModelMapper modelMapper;

    @Autowired
    public IndicatorService(IndicatorRepository indicatorRepository, PeopleService peopleService, IndicatorGroupService groupService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.groupService = groupService;
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

    // ToDo: Сделать правильное разбиение по группам индикаторов

    public ResponseEntity<Map<String, SimpleIndicatorResponceDTO>> getSimpleDTOByGroups(int personId) {
        Person person = peopleService.findById(personId);
        List<IndicatorGroup> groups = groupService.find();
        List<Indicator> indicators = this.findAllCorrectIndicators(person);
        indicators.stream().map(Indicator::getRusName).forEach(System.out::println);

        Map<String, SimpleIndicatorResponceDTO> indicatorsByGroups = new HashMap<>();

        for (IndicatorGroup group : groups) {
            for (String indicatorName : group.getIndicators()) {
                for (Indicator indicator : indicators) {

                    if (indicator.getEngName().equals(indicatorName)){
                        indicatorsByGroups.put(group.getGroupName(), convertToSimpleDTO(indicator));
                    }

                }
            }
        }

        return ok(indicatorsByGroups
                .entrySet()
                .stream()
                .sorted(comparingByValue(comparingInt(SimpleIndicatorResponceDTO::getId)))
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, _) -> oldValue,
                        LinkedHashMap::new)));
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

    protected List<Indicator> findAllCorrectIndicators(Person person) {
        int age = getDaysOfAge(person.getDateOfBirth());
        return indicatorRepository.findAllCorrect(person.getGender(), person.getIsGravid(), age);
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

    private SimpleIndicatorResponceDTO convertToSimpleDTO(Indicator indicator) {
        return modelMapper.map(indicator, SimpleIndicatorResponceDTO.class);
    }
}
