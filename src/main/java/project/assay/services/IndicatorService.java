package project.assay.services;

import static project.assay.utils.converters.StaticMethods.getDaysOfAge;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;

@Service
public class IndicatorService {
  private final IndicatorRepository indicatorRepository;
  private final PeopleService peopleService;

  @Autowired
  public IndicatorService(IndicatorRepository indicatorRepository, PeopleService peopleService) {
    this.indicatorRepository = indicatorRepository;
    this.peopleService = peopleService;
  }

  public List<Indicator> findAll(){
    return indicatorRepository.findAll();
  }

  public Indicator findById(int id) {
    return indicatorRepository.getIndicatorById(id);
  }

  public Indicator findOneCorrect(String indicatorName, Person person) {
    String gender = person.getGender();
    int age = getDaysOfAge(person.getDateOfBirth());
    return indicatorRepository.findOneCorrect(indicatorName, gender, age);
  }


  public List<Indicator> findAllCorrect(Person person) {
    int age = getDaysOfAge(person.getDateOfBirth());
    return indicatorRepository.findAllCorrect(person.getGender(), age);
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
    }
    else if (value > maxValue) {
      return "upper";
    }
    else {
      return "ok";
    }
  }
}
