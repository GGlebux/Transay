package project.assay.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.Indicator;
import project.assay.repositories.IndicatorRepository;

@Service
public class IndicatorService {
  private final IndicatorRepository indicatorRepository;

  @Autowired
  public IndicatorService(IndicatorRepository indicatorRepository) {
    this.indicatorRepository = indicatorRepository;
  }

  public List<Indicator> findAll(){
    return indicatorRepository.findAll();
  }

  public Indicator findOne(){
    return this.findAll().stream().findAny().orElse(null);
  }
}
