package project.assay.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public Indicator save(Indicator indicator) {
    return indicatorRepository.save(indicator);
  }

  public List<Indicator> findAllCorrect(Person person) {
    int age = peopleService.getDaysOfAge(person.getId());
    String gender = person.getGender();
    return indicatorRepository.findAllByGenderAndAge(gender, age);
  }

  public Indicator findOne(){
    return this.findAll().stream().findAny().orElse(null);
  }
}
