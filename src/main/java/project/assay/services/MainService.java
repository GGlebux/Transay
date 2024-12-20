//package project.assay.services;
//
//import java.util.List;
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import project.assay.exceptions.IndicatorNotFoundException;
//import project.assay.models.Indicator;
//import project.assay.models.Person;
//import project.assay.models.PersonInfo;
//import project.assay.repositories.MainRepository;
//
//@Service
//@Transactional(readOnly = true)
//public class MainService {
//
//  private final MainRepository mainRepository;
//  private final PeopleService peopleService;
//  private final IndicatorService indicatorService;
//
//  @Autowired
//  public MainService(MainRepository mainRepository, PeopleService peopleService,
//      IndicatorService indicatorService) {
//    this.mainRepository = mainRepository;
//    this.peopleService = peopleService;
//    this.indicatorService = indicatorService;
//  }
//
//  public PersonInfo findById(int id) {
//    Optional<PersonInfo> personIndicator = mainRepository.findById(id);
//    return personIndicator.orElseThrow(IndicatorNotFoundException::new);
//  }
//
//  public List<Indicator> findOwnerIndicator(int personId) {
//    List<PersonInfo> indicators = mainRepository.findPersonIndicatorByPersonId(personId);
//    return indicators.stream().map(PersonInfo::getIndicator).toList();
//  }
//
//  public List<PersonInfo> findAll() {
//    return mainRepository.findAll();
//  }
//
//  public List<PersonInfo> findPI(int id) {
//    return mainRepository.findPersonIndicatorByPersonId(id);
//  }
//
//  @Transactional
//  public void save(PersonInfo personIndicator) {
//    mainRepository.save(personIndicator);
//  }
//
//  @Transactional
//  public void updateValue(int id, PersonInfo personIndicator) {
//    mainRepository.updateCurrentValueById(id, personIndicator.getCurrentValue());
//  }
//
//  @Transactional
//  public void saveByObjects(Person person, Indicator indicator, Double value) {
//    PersonInfo pi = new PersonInfo();
//    pi.setIndicator(indicator);
//    pi.setPerson(person);
//    pi.setCurrentValue(value);
//    mainRepository.save(pi);
//  }
//
//  @Transactional
//  public void saveByIds(int personId, int indicatorId, Double value) {
//    PersonInfo pi = new PersonInfo();
//    pi.setIndicator(indicatorService.findById(indicatorId));
//    pi.setPerson(peopleService.findById(personId));
//    pi.setCurrentValue(value);
//    mainRepository.save(pi);
//  }
//
//  @Transactional
//  public void delete(int id) {
//    mainRepository.deleteById(id);
//  }
//
//  public Person getPersonById(int pid) {
//    Optional<PersonInfo> personIndicator = mainRepository.findById(pid);
//    return personIndicator.map(PersonInfo::getPerson).orElse(null);
//  }
//
//  public Indicator getIndicatorById(int pid) {
//    Optional<PersonInfo> personIndicator = mainRepository.findById(pid);
//    return personIndicator.map(PersonInfo::getIndicator).orElse(null);
//  }
//
//  public Double getCurrentValueById(int pid) {
//    Optional<PersonInfo> personIndicator = mainRepository.findById(pid);
//    return personIndicator.map(PersonInfo::getCurrentValue).orElse(null);
//  }
//
//
//}
