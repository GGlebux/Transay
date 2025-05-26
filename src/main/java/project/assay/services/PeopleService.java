package project.assay.services;


import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.ExcludedReasonDTO;
import project.assay.dto.PersonDTO;
import project.assay.dto.PersonUpdateDTO;
import project.assay.exceptions.PersonNotFoundException;
import project.assay.models.ExcludedReason;
import project.assay.models.Person;
import project.assay.repositories.PeopleRepository;

import static java.net.URI.create;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.ResponseEntity.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public PeopleService(PeopleRepository peopleRepository, ModelMapper modelMapper) {
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
    }

    public Person find(int id){
        return peopleRepository
                .findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person with id=" + id + " not found!"));
    }

    public ResponseEntity<PersonDTO> findById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            return ok(converToPersonDTO(person.get()));
        }
        throw new PersonNotFoundException("Person with id=" + id + " not found!");
    }

    @Transactional
    public ResponseEntity<String> save(PersonDTO personDTO) {
        Person saved = peopleRepository.save(convertToPerson(personDTO));
        return created(create("/people/" + saved.getId()))
                .body("Created person with id=" + saved.getId());

    }

    @Transactional
    public ResponseEntity<HttpStatus> update(int id, PersonUpdateDTO personDTOtoUpdate) {
        Optional<Person> personFromDB = peopleRepository.findById(id);
        if (personFromDB.isPresent()) {
            Person personToUpdate = convertToPerson(personDTOtoUpdate, personFromDB.get());
            personToUpdate.setId(id);
            peopleRepository.save(personToUpdate);
            return ok(ACCEPTED);
        }
        throw new PersonNotFoundException("Person with id=" + id + " not found!");
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person with id=" + id + " not found!"));
        peopleRepository.deleteById(id);
        return noContent().build();
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private Person convertToPerson(PersonUpdateDTO personUpdateDTO, Person preparedPerson) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(personUpdateDTO, preparedPerson);
        return preparedPerson;
    }

    private PersonDTO converToPersonDTO(Person person) {
        List<ExcludedReasonDTO> reasons = converToReasonDTO(person.getExcludedExcludedReasons());
        PersonDTO personDTO = modelMapper.map(person, PersonDTO.class);
        personDTO.setExcludedReasons(reasons);
        return personDTO;
    }

    private List<ExcludedReasonDTO> converToReasonDTO(List<ExcludedReason> excludedReasons) {
        return excludedReasons
                .stream()
                .map((reason -> modelMapper.map(reason, ExcludedReasonDTO.class)))
                .toList();
    }
}