package project.assay.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.PersonNotFoundException;
import project.assay.models.Person;
import project.assay.repositories.PeopleRepository;

@Service
@Transactional(readOnly = true)
public class PeopleService {

  private final PeopleRepository peopleRepository;


  @Autowired
  public PeopleService(PeopleRepository peopleRepository) {
    this.peopleRepository = peopleRepository;
  }

  public Person findById(int id) {
    // ToDo найти по id, а не любого
    Optional<Person> person = peopleRepository.findById(id);
    return person.orElseThrow(PersonNotFoundException::new);
  }

  @Transactional
  public Person save(Person person) {
    return peopleRepository.save(person);
  }

  @Transactional
  public void update(int id, Person person) {
    person.setId(id);
    peopleRepository.save(person);
  }

  @Transactional
  public void delete(int id) {
    peopleRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    peopleRepository.deleteById(id);
  }

  public int getDaysOfAge(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    if (person.isPresent()) {
      LocalDate birthDate = person.get().getDateOfBirth();
      LocalDate today = LocalDate.now();
      return (int) ChronoUnit.DAYS.between(birthDate, today);
    }
    return 0;
  }
}
