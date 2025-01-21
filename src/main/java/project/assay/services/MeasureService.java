package project.assay.services;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Measure;
import project.assay.repositories.MeasureRepository;

@Service
public class MeasureService {

  private final MeasureRepository measureRepository;

  public MeasureService(MeasureRepository measureRepository) {
    this.measureRepository = measureRepository;
  }

  public List<Measure> findAllById(int personId) {
    return measureRepository.findByPersonId(personId);
  }

  public Measure findById(int measureId) {
    return measureRepository.findById(measureId).orElse(null);
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

  @Transactional
  public int save(Measure measure) {
    Measure measureId = measureRepository.save(measure);
    return measureId.getId();
  }
}
