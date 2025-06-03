package project.assay.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.IndicatorRequestDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.utils.StaticMethods.*;

@Service
@Transactional(readOnly = true)
public class IndicatorService {

    private final IndicatorRepository indicatorRepository;
    private final ModelMapper modelMapper;
    private static final String UNITS_PATH = "src/main/resources/static/units.xlsx";


    @Autowired
    public IndicatorService(IndicatorRepository indicatorRepository, ModelMapper modelMapper) {
        modelMapper
                .createTypeMap(IndicatorRequestDTO.class, Indicator.class)
                .addMappings(mapper -> {
            mapper.skip(Indicator::setMaxAge);
            mapper.skip(Indicator::setMinAge);
        });
        this.indicatorRepository = indicatorRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<List<Indicator>> findAll() {
        return status(OK).body(indicatorRepository.findAll());
    }

    public Indicator findById(int id) {
        return indicatorRepository.getIndicatorById(id)
                .orElseThrow(() -> new EntityNotFoundException("Indicator with id=" + id + " not found"));
    }

    public ResponseEntity<Set<String>> findAllUnits(){
        return ok(parseExcelColumn(UNITS_PATH, 0));
    }

    public List<Indicator> findAllCorrect(Person person) {
        int age = getDaysOfAge(person.getDateOfBirth());
        return indicatorRepository.findAllCorrect(person.getGender(), person.getIsGravid(), age);
    }

    @Transactional
    public ResponseEntity<Indicator> create(IndicatorRequestDTO dto) {
        return ok(indicatorRepository.save(convertToEntity(dto)));
    }

    @Transactional
    public Indicator save(Indicator indicator) {
        return indicatorRepository.save(indicator);
    }

    public String checkValue(Indicator indicator, double value) {
        double minValue = indicator.getMinValue();
        double maxValue = indicator.getMaxValue();
        if (value < minValue) {
            return "lower";
        } else if (value > maxValue) {
            return "upper";
        } else {
            return "ok";
        }
    }

    private Indicator convertToEntity(IndicatorRequestDTO dto) {
        Indicator indicator = new Indicator();
        modelMapper.map(dto, indicator);
        indicator.setMinAge(getTotalDays(dto.getMinAge()));
        indicator.setMaxAge(getTotalDays(dto.getMaxAge()));
        return indicator;
    }
}
