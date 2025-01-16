package project.assay.services;

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

  public Indicator findOneCorrect(String indicatorName, int personId) {
    Person person = peopleService.findById(personId);
    String gender = person.getGender();
    int age = peopleService.getDaysOfAge(personId);
    return indicatorRepository.findOneCorrect(indicatorName, gender, age);
  }


  public List<Indicator> findAllCorrect(Person person) {
    int age = peopleService.getDaysOfAge(person.getId());
    return indicatorRepository.findAllCorrect(person.getGender(), age);
  }

  @Transactional
  public Indicator save(Indicator indicator) {
    return indicatorRepository.save(indicator);
  }
}
