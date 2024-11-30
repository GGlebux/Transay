package project.assay.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Person;
import project.assay.repositories.IndicatorRepository;
import project.assay.repositories.PeopleRepository;
import project.assay.repositories.MainRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

  private final PeopleRepository peopleRepository;
  private final IndicatorRepository indicatorRepository;
  private final MainRepository mainRepository;

  @Autowired
  public PeopleService(PeopleRepository peopleRepository, IndicatorRepository indicatorRepository,
      MainRepository mainRepository) {
    this.peopleRepository = peopleRepository;
    this.indicatorRepository = indicatorRepository;
    this.mainRepository = mainRepository;
  }

  public List<Person> findAll() {
    return peopleRepository.findAll();
  }

  public Optional<Person> findOne() {
    return this.findAll().stream().findAny();
  }

  public Person findById(int id) {
    return peopleRepository.findById(id).orElse(null);
  }

  @Transactional
  public void save(Person person) {
    peopleRepository.save(person);
  }

  @Transactional
  public void update(int id, Person person) {
    person.setId(id);
    peopleRepository.save(person);
  }

  @Transactional
  public void delete(int id) {
    peopleRepository.deleteById(id);
  }

  public int getDaysOfAge(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    if (person.isPresent()) {
      LocalDate birthDate = person.get().getDateOfBirth();
      LocalDate today = LocalDate.now();
      System.out.println(ChronoUnit.DAYS.between(birthDate, today));
      return (int) ChronoUnit.DAYS.between(birthDate, today);
    }
    return 0;
  }
}
