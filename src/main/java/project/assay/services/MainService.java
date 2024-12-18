package project.assay.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.IndicatorNotFoundException;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.PersonIndicator;
import project.assay.repositories.MainRepository;

@Service
@Transactional(readOnly = true)
public class MainService {

  private final MainRepository mainRepository;
  private final PeopleService peopleService;
  private final IndicatorService indicatorService;

  @Autowired
  public MainService(MainRepository mainRepository, PeopleService peopleService,
      IndicatorService indicatorService) {
    this.mainRepository = mainRepository;
    this.peopleService = peopleService;
    this.indicatorService = indicatorService;
  }

  public PersonIndicator findById(int id) {
    Optional<PersonIndicator> personIndicator = mainRepository.findById(id);
    return personIndicator.orElseThrow(IndicatorNotFoundException::new);
  }

  public List<Indicator> findOwnerIndicator(int personId) {
    List<PersonIndicator> indicators = mainRepository.findPersonIndicatorByPersonId(personId);
    return indicators.stream().map(PersonIndicator::getIndicator).toList();
  }

  public List<PersonIndicator> findAll() {
    return mainRepository.findAll();
  }

  public List<PersonIndicator> findPI(int id) {
    return mainRepository.findPersonIndicatorByPersonId(id);
  }

  @Transactional
  public void save(PersonIndicator personIndicator) {
    mainRepository.save(personIndicator);
  }

  @Transactional
  public void updateValue(int id, PersonIndicator personIndicator) {
    mainRepository.updateCurrentValueById(id, personIndicator.getCurrentValue());
  }

  @Transactional
  public void saveByObjects(Person person, Indicator indicator, Double value) {
    PersonIndicator pi = new PersonIndicator();
    pi.setIndicator(indicator);
    pi.setPerson(person);
    pi.setCurrentValue(value);
    mainRepository.save(pi);
  }

  @Transactional
  public void saveByIds(int personId, int indicatorId, Double value) {
    PersonIndicator pi = new PersonIndicator();
    pi.setIndicator(indicatorService.findById(indicatorId));
    pi.setPerson(peopleService.findById(personId));
    pi.setCurrentValue(value);
    mainRepository.save(pi);
  }

  @Transactional
  public void delete(int id) {
    mainRepository.deleteById(id);
  }

  public Person getPersonById(int pid) {
    Optional<PersonIndicator> personIndicator = mainRepository.findById(pid);
    return personIndicator.map(PersonIndicator::getPerson).orElse(null);
  }

  public Indicator getIndicatorById(int pid) {
    Optional<PersonIndicator> personIndicator = mainRepository.findById(pid);
    return personIndicator.map(PersonIndicator::getIndicator).orElse(null);
  }

  public Double getCurrentValueById(int pid) {
    Optional<PersonIndicator> personIndicator = mainRepository.findById(pid);
    return personIndicator.map(PersonIndicator::getCurrentValue).orElse(null);
  }


}
