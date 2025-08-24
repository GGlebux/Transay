package project.assay.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.PersonRequestDTO;
import project.assay.dto.responses.PersonResponseDTO;
import project.assay.exceptions.EntityNotCreatedException;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.repositories.PeopleRepository;

import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparingInt;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static project.assay.utils.StaticMethods.isFuture;

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

    public ResponseEntity<List<PersonResponseDTO>> findAll() {
        return ok(peopleRepository
                .findAll()
                .stream()
                .sorted(comparingInt(Person::getId))
                .map(this::convertToResponseDTO)
                .toList());
    }

    public Person findById(int id){
        return peopleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person with id=" + id + " not found!"));
    }

    public ResponseEntity<PersonResponseDTO> find(int id) {
        return ok(convertToResponseDTO(findById(id)));
    }

    @Transactional
    public ResponseEntity<PersonResponseDTO> save(PersonRequestDTO dto) {
        boolean isMale = dto.getGender().equals("male");

        validatePerson(dto);

        Person saved = peopleRepository.save(convertToEntity(dto));
        if (isMale) {
            PersonResponseDTO realMan = beRealMan(saved);
            return ok(realMan);
        }

        return status(CREATED).body(convertToResponseDTO(saved));

    }

    @Transactional
    public ResponseEntity<PersonResponseDTO> update(int id, PersonRequestDTO personRequestDTO) {
        Person personFromDB = this.findById(id);

        Person personToUpdate = convertToEntity(personRequestDTO, personFromDB);
        personToUpdate.setId(id);

        validatePerson(convertToRequestDTO(personToUpdate));

        return ok(convertToResponseDTO(peopleRepository.save(personToUpdate)));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        peopleRepository.deleteById(id);
        return status(NO_CONTENT).build();
    }

    @Transactional
    public PersonResponseDTO beRealMan(Person man) {
        Set<Reason> maleExReasons = reasonsService
                .findByNameIn(Set
                        .of("беременность", "менструация"));
        man
                .getExcludedReasons()
                .addAll(maleExReasons);
        return convertToResponseDTO(peopleRepository.save(man));
    }

    public Set<Reason> findAllEx(int personId) {
        Person person = this.findById(personId);
        return person.getExcludedReasons();
    }

    public ResponseEntity<Set<Reason>> findAllExWithResponse(int personId) {
        return ok(this.findAllEx(personId));
    }

    @Transactional
    public ResponseEntity<Set<Reason>> createEx(int personId, int reasonId) {
        return this.createManyEx(personId, Set.of(reasonId));
    }

    @Transactional
    public ResponseEntity<Set<Reason>> createManyEx(int personId, Set<Integer> reasonIds) {
        Person person = this.findById(personId);
        Set<Reason> reasons = reasonsService.findByIdIn(reasonIds);
        person.getExcludedReasons().addAll(reasons);
        return ok(peopleRepository
                .save(person)
                .getExcludedReasons());
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteEx(int personId, int reasonId) {
        Person person = this.findById(personId);
        Reason reason = reasonsService.findById(reasonId);
        person.getExcludedReasons().remove(reason);
        peopleRepository.save(person);
        return status(NO_CONTENT).build();
    }

    private Person convertToEntity(PersonRequestDTO personRequestDTO) {
        return modelMapper.map(personRequestDTO, Person.class);
    }

    private Person convertToEntity(PersonRequestDTO personToUpdateDTO, Person preparedPerson) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.map(personToUpdateDTO, preparedPerson);
        return preparedPerson;
    }

    private PersonRequestDTO convertToRequestDTO(Person person) {
        return modelMapper.map(person, PersonRequestDTO.class);
    }

    private void validatePerson(PersonRequestDTO man){
        boolean isMale = man.getGender().equals("male");

        if (man.getIsGravid() && isMale) {
            throw new EntityNotCreatedException("Gender Male should be isGravid");
        }

        if (isFuture(man.getDateOfBirth())) {
            throw new EntityNotCreatedException("Date of Birth should be past");
        }

    }

    private PersonResponseDTO convertToResponseDTO(Person person) {
        return modelMapper.map(person, PersonResponseDTO.class);
    }
}