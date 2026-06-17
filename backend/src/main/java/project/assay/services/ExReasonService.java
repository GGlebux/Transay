package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Person;
import project.assay.models.Reason;
import project.assay.repositories.PeopleRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ExReasonService {
    private final CustomerService customerService;
    private final ReasonsService reasonsService;
    private final PeopleRepository peopleRepository;
    private final PeopleService peopleService;

    @Autowired
    public ExReasonService(CustomerService customerService, ReasonsService reasonsService,
                           PeopleRepository peopleRepository, PeopleService peopleService) {
        this.customerService = customerService;
        this.reasonsService = reasonsService;
        this.peopleRepository = peopleRepository;
        this.peopleService = peopleService;
    }

    public List<Reason> findAllExWithResponse() {
        return findAllExFor(customerService.getPersonFromAuth());
    }

    public List<Reason> findAllExForPerson(long personId) {
        return findAllExFor(peopleService.getOwnedPerson(personId));
    }

    @Transactional
    public List<Reason> addExcludedReasons(Set<Integer> ids) {
        return addExcludedReasonsFor(customerService.getPersonFromAuth(), ids);
    }

    @Transactional
    public List<Reason> addExcludedReasonsForPerson(long personId, Set<Integer> ids) {
        return addExcludedReasonsFor(peopleService.getOwnedPerson(personId), ids);
    }

    @Transactional
    public void deleteEx(int reasonId) {
        deleteExFor(customerService.getPersonFromAuth(), reasonId);
    }

    @Transactional
    public void deleteExForPerson(long personId, int reasonId) {
        deleteExFor(peopleService.getOwnedPerson(personId), reasonId);
    }

    private List<Reason> findAllExFor(Person person) {
        return person.getExcludedReasons().stream().toList();
    }

    private List<Reason> addExcludedReasonsFor(Person person, Set<Integer> ids) {
        Set<Reason> allFromDB = reasonsService.findByIdIn(ids);
        person.getExcludedReasons().addAll(allFromDB);
        return person.getExcludedReasons().stream().toList();
    }

    private void deleteExFor(Person person, int reasonId) {
        Reason fromDB = reasonsService.findById(reasonId);
        person.getExcludedReasons().remove(fromDB);
        peopleRepository.save(person);
    }
}
