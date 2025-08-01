package project.assay.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.requests.PersonToUpdateDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.repositories.PeopleRepository;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;
    private final ReasonsService reasonsService;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper modelMapper, ReasonsService reasonsService) {
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
        this.reasonsService = reasonsService;
    }

    public Person findById(int id){
        return peopleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person with id=" + id + " not found!"));
    }

    public ResponseEntity<Person> find(int id) {
        return ok(findById(id));
    }

    @Transactional
    public ResponseEntity<Person> save(PersonRequestDTO personRequestDTO) {
        Person saved = peopleRepository.save(convertToPerson(personRequestDTO));
        return status(CREATED).body(saved);

    }

    @Transactional
    public ResponseEntity<Person> update(int id, PersonToUpdateDTO personDTOtoUpdate) {
        Person personFromDB = this.findById(id);

        Person personToUpdate = convertToPerson(personDTOtoUpdate, personFromDB);
        personToUpdate.setId(id);
        return ok(peopleRepository.save(personToUpdate));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        peopleRepository.deleteById(id);
        return status(NO_CONTENT).build();
    }

    public List<Reason> findAllExReasons(int personId) {
        Person person = this.findById(personId);
        return person.getExcludedReasons();
    }

    public ResponseEntity<List<Reason>> findAllEx(int personId) {
        return ok(this.findAllExReasons(personId));
    }

    @Transactional
    public ResponseEntity<Reason> createEx(int personId, int reasonId) {
        Person person = this.findById(personId);
        Reason reason = reasonsService.findById(reasonId);
        person.getExcludedReasons().add(reason);
        return ok(peopleRepository
                .save(person)
                .getExcludedReasons()
                .stream()
                .filter(elem -> elem.getId() == reasonId)
                .findFirst().get());
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteEx(int personId, int reasonId) {
        Person person = this.findById(personId);
        Reason reason = reasonsService.findById(reasonId);
        person.getExcludedReasons().remove(reason);
        peopleRepository.save(person);
        return status(NO_CONTENT).build();
    }

    private Person convertToPerson(PersonRequestDTO personRequestDTO) {
        return modelMapper.map(personRequestDTO, Person.class);
    }

    private Person convertToPerson(PersonToUpdateDTO personToUpdateDTO, Person preparedPerson) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(personToUpdateDTO, preparedPerson);
        return preparedPerson;
    }
}