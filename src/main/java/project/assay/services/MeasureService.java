package project.assay.services;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.MeasureUpdateDTO;
import project.assay.models.Indicator;
import project.assay.models.Measure;
import project.assay.models.Person;
import project.assay.models.Referent;
import project.assay.repositories.IndicatorRepository;
import project.assay.repositories.MeasureRepository;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final IndicatorService indicatorService;

    public MeasureService(MeasureRepository measureRepository, IndicatorService indicatorService) {
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
    }

    public List<Measure> findAllByPersonId(int personId) {
        return measureRepository.findByPersonId(personId);
    }

    public Map<String, List<String>> getDecryptedMeasures(int personId, LocalDate date) {
        List<Measure> measures = measureRepository.findByPersonIdAndDate(personId, date);
        Map<String, List<String>> decryptedMeasures = new HashMap<>();
        for (Measure measure : measures) {
            Referent referent = measure.getReferent();
            if (referent.getStatus().equals("ok")) {
                continue;
            }

            String name = measure.getIndicator().getName();
            List<String> reasons = referent.getReasons();
            if (decryptedMeasures.containsKey(name)) {
                decryptedMeasures.get(name).addAll(reasons);
            } else {
                decryptedMeasures.put(name, reasons);
            }
        }
        return decryptedMeasures;
    }


    public Measure findById(int measureId) {
        return measureRepository.findById(measureId).orElse(null);
    }


    @Transactional
    public int save(Measure measure) {
        Measure measureId = measureRepository.save(measure);
        return measureId.getId();
    }

    @Transactional
    public void delete(Measure measure) {
        measureRepository.delete(measure);
    }

    @Transactional
    public void deleteById(int measureId) {
        if (!measureRepository.existsById(measureId)) {
            throw new NoSuchElementException("Measure with id=" + measureId + " not found.");
        }
        measureRepository.deleteById(measureId);
    }

    public String canCreateMeasure(Person person, MeasureUpdateDTO verifiableMeasure) {
        int selectedId = verifiableMeasure.getSelectedId();
        List<Indicator> indicators = indicatorService.findAllCorrect(person);

        Set<Integer> idCorrectIndicators = indicators
                .stream()
                .map(Indicator::getId)
                .collect(Collectors.toSet());
        if (!idCorrectIndicators.contains(selectedId)) {
            return "Incorrect selection indicator id!";
        }

        Set<LocalDate> referentsDates = person.getMeasureList()
                .stream()
                .map(Measure::getReferent)
                .map(Referent::getRegDate)
                .collect(Collectors.toSet());
        if (referentsDates.contains(verifiableMeasure.getRegDate())) {
            return "Selected Indicator with this date already exists!";
        }
        return "ok";
    }
}
