package project.assay.services;


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
    Optional<Person> person = peopleRepository.findById(id);
    return person.orElseThrow(
        () -> new PersonNotFoundException("Person with id=" + id + " not found!"));
  }

  @Transactional
  public Person save(Person person) {
    return peopleRepository.save(person);
  }

  @Transactional
  public void update(int id, Person person) {
    Optional<Person> personFromDB = peopleRepository.findById(id);
    personFromDB.orElseThrow(
        () -> new PersonNotFoundException("Person with id=" + id + " not found!"));
    person.setId(id);
    peopleRepository.save(person);
  }

  @Transactional
  public void delete(int id) {
    peopleRepository.findById(id)
        .orElseThrow(() -> new PersonNotFoundException("Person with id=" + id + " not found!"));
    peopleRepository.deleteById(id);
  }


}