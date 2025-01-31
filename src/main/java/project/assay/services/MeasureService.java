package project.assay.services;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.MeasureUpdateDTO;
import project.assay.models.*;
import project.assay.repositories.MeasureRepository;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final IndicatorService indicatorService;
    private final ExcludedReasonService excludedReasonService;

    public MeasureService(MeasureRepository measureRepository, IndicatorService indicatorService, ExcludedReasonService excludedReasonService) {
        this.measureRepository = measureRepository;
        this.indicatorService = indicatorService;
        this.excludedReasonService = excludedReasonService;
    }

    public List<Measure> findAllByPersonId(int personId) {
        return measureRepository.findByPersonId(personId);
    }

    public Map<String, Double> getDecryptedMeasures(int personId, LocalDate date) {
        List<Measure> measures = measureRepository.findByPersonIdAndDate(personId, date);
        List<String> excludedReasons = excludedReasonService.findByPersonId(personId)
                .stream()
                .map(ExcludedReason::getReason).toList();

        Map<String, Double> decryptedMeasures = new HashMap<>();
        int counter = 0;
        for (Measure measure : measures) {
            Referent referent = measure.getReferent();
            if (referent.getStatus().equals("ok")) {
                continue;
            }
            List<String> personReasons = referent.getReasons();
            for (String reason : personReasons) {
                if (excludedReasons.contains(reason)) {
                    continue;
                }
                if (decryptedMeasures.containsKey(reason)) {
                    decryptedMeasures.merge(reason, 1.0, Double::sum);
                } else {
                    decryptedMeasures.put(reason, 1.0);
                }
                counter++;
            }
        }
        for (Map.Entry<String, Double> entry : decryptedMeasures.entrySet()) {
            Double newValue = Math.round(entry.getValue() / counter * 1000.0) / 1000.0;
            entry.setValue(newValue);
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

        Indicator selectedIndicator = indicatorService.findById(selectedId);
        Set<LocalDate> referentsDates = person.getMeasureList()
                .stream()
                .filter(measure -> measure.getIndicator().getName().equals(selectedIndicator.getName()))
                .map(Measure::getReferent)
                .map(Referent::getRegDate)
                .collect(Collectors.toSet());
        if (referentsDates.contains(verifiableMeasure.getRegDate())) {
            return "Selected Indicator with this date already exists!";
        }
        return "ok";
    }
}
