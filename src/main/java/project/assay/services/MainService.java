package project.assay.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.Indicator;
import project.assay.models.IndicatorWithCurrentValue;
import project.assay.models.PersonIndicator;
import project.assay.repositories.IndicatorRepository;
import project.assay.repositories.PersonIndicatorRepository;

@Service
public class MainService {
  private final PersonIndicatorRepository bindingRepository;
  private final IndicatorRepository indicatorRepository;
  private final PeopleService peopleService;
  private final IndicatorService indicatorService;

  @Autowired
  public MainService(PersonIndicatorRepository bindingRepository,
      IndicatorRepository indicatorRepository, PeopleService peopleService,
      IndicatorService indicatorService) {
    this.bindingRepository = bindingRepository;
    this.indicatorRepository = indicatorRepository;
    this.peopleService = peopleService;
    this.indicatorService = indicatorService;
  }

  // Возвращает список Индикатор - Значение по Id человека
  public List<IndicatorWithCurrentValue> getIndicatorsById(int id) {

    List<PersonIndicator> personIndicators = bindingRepository.findByPersonId(id);
    List<IndicatorWithCurrentValue> fullIndicators = new ArrayList<>();

    for (PersonIndicator personIndicator : personIndicators) {
      Indicator indicator = indicatorRepository.getIndicatorById(
          personIndicator.getId().getIndicatorId());
      fullIndicators.add(
          new IndicatorWithCurrentValue(indicator, personIndicator.getCurrentValue()));
    }
    return fullIndicators;
  }
  // Возращает введенное значение конкретного пары человека и индикаторы
  public Double getCurrentValue(int personId, int indicatorId) {
    return bindingRepository.findCurrentValue(personId, indicatorId);
  }
}
