package project.assay.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import static java.lang.String.format;
import static java.util.Comparator.comparingLong;
import static java.util.Objects.nonNull;
import static java.util.Set.of;
import static project.assay.AssayApplication.DATE_FORMAT;
import static project.assay.models.enums.Condition.BASE;
import static project.assay.models.enums.Condition.GRAVID;
import static project.assay.models.enums.PersonGender.MALE;
import static project.assay.utils.StaticMethods.isFuture;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;
    private final ReasonsService reasonsService;
    private final CustomerService customerService;
    private static final Set<String> maleExReasons;
    private static final Set<String> femaleExReasons;

    static {
        maleExReasons = of("беременность",
                "менструация",
                "поликистоз яичников");
        femaleExReasons = of("беременность");
    }


    @Autowired
    public PeopleService(PeopleRepository peopleRepository,
                         ModelMapper modelMapper,
                         ReasonsService reasonsService,
                         CustomerService customerService) {
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
        this.reasonsService = reasonsService;
        this.customerService = customerService;
    }

    public List<PersonResponseDTO> findAll() {
        return peopleRepository
                .findAll()
                .stream()
                .sorted(comparingLong(Person::getId))
                .map(this::convertToResponseDTO)
                .toList();
    }

    public Person findById(Long id) {
        return peopleRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Человек с id='%d' не найден!", id)));
    }

    public PersonResponseDTO find() {
        Person person = customerService.getPersonFromAuth();
        return convertToResponseDTO(person);
    }

    @Transactional
    public PersonResponseDTO save(PersonRequestDTO dto) {
        validatePerson(dto);

        var customer = customerService.getAuthenticatedCustomer();
        if (nonNull(customer.getPerson())){
            throw new EntityNotCreatedException(
                    "Личные данные пользователя '%s' уже заполнены"
                            .formatted(customer.getEmail())
            );
        }


        var personToSave = convertToEntity(dto);
        personToSave.setCustomer(customer);
        customer.setPerson(personToSave);

        Person saved = peopleRepository.save(personToSave);
        customerService.save(customer);

        return switch (dto.getGender()) {
            case MALE -> beRealMan(saved, maleExReasons);
            case FEMALE -> beRealWoman(saved, femaleExReasons);
        };
    }

    @Transactional
    public PersonResponseDTO update(PersonRequestDTO personRequestDTO) {
        Person personFromDB = customerService.getPersonFromAuth();

        Person personToUpdate = convertToEntity(personRequestDTO, personFromDB);
        personToUpdate.setId(personFromDB.getId());
        personToUpdate.setExcludedReasons(personFromDB.getExcludedReasons());

        validatePerson(convertToRequestDTO(personToUpdate));

        return convertToResponseDTO(peopleRepository.save(personToUpdate));
    }

    @Transactional
    public void delete() {
        Person person = customerService.getPersonFromAuth();
        peopleRepository.deleteById(person.getId());
    }

    @Transactional
    public PersonResponseDTO beRealMan(Person man, Set<String> exReasons) {
        Set<Reason> maleExReasons = reasonsService
                .findByNameIn(exReasons);
        man
                .getExcludedReasons()
                .addAll(maleExReasons);
        return convertToResponseDTO(peopleRepository.save(man));
    }

    @Transactional
    public PersonResponseDTO beRealWoman(Person woman, Set<String> exReasons) {
        // Если беременна, то не добавляем исключенные причины
        if (woman.getCondition().equals(GRAVID)){
            return convertToResponseDTO(woman);
        }
        Set<Reason> femaleExReasons = reasonsService
                .findByNameIn(exReasons);
        woman
                .getExcludedReasons()
                .addAll(femaleExReasons);
        return convertToResponseDTO(peopleRepository.save(woman));
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

    private void validatePerson(PersonRequestDTO man) {
        if (man.getGender().equals(MALE)
                && !man.getCondition().equals(BASE)) {
            throw new EntityNotCreatedException("Мужчина не может быть беременным!");
        }

        if (isFuture(man.getDateOfBirth())) {
            throw new EntityNotCreatedException(
                    format("Дата рождения (%s) не может быть в будущем!",
                            DATE_FORMAT.format(man.getDateOfBirth())));
        }

    }

    private PersonResponseDTO convertToResponseDTO(Person person) {
        return modelMapper.map(person, PersonResponseDTO.class);
    }
}