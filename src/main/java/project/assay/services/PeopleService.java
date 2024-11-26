package project.assay.services;

import jakarta.validation.Valid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.assay.models.Indicator;
import project.assay.models.IndicatorWithCurrentValue;
import project.assay.models.Person;
import project.assay.models.PersonIndicator;
import project.assay.repositories.IndicatorRepository;
import project.assay.repositories.PeopleRepository;
import project.assay.repositories.PersonIndicatorRepository;

@Service
public class PeopleService {

  private final PeopleRepository peopleRepository;
  private final IndicatorRepository indicatorRepository;
  private final PersonIndicatorRepository personIndicatorRepository;

  @Autowired
  public PeopleService(PeopleRepository peopleRepository, IndicatorRepository indicatorRepository,
      PersonIndicatorRepository personIndicatorRepository) {
    this.peopleRepository = peopleRepository;
    this.indicatorRepository = indicatorRepository;
    this.personIndicatorRepository = personIndicatorRepository;
  }

  public List<Person> findAll() {
    return peopleRepository.findAll();
  }

  public Person findOne() {
    return this.findAll().stream().findAny().orElse(null);
  }

  public Person findById(int id) {
    return peopleRepository.findById(id).orElse(null);
  }

  public void save(Person person) {
    peopleRepository.save(person);
  }

  public void update(int id, Person person) {
    person.setId(id);
    peopleRepository.save(person);
  }

  public void delete(int id) {
    peopleRepository.deleteById(id);
  }



  public int getDaysOfAge(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    if (person.isPresent()) {
      Date birthDate = person.get().getDateOfBirth();
      Date currentDate = new Date();
      return (int) Duration.between(birthDate.toInstant(), currentDate.toInstant()).toDays();
    }
    return 0;
  }
}
