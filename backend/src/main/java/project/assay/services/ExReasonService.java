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

    @Autowired
    public ExReasonService(CustomerService customerService, ReasonsService reasonsService, PeopleRepository peopleRepository) {
        this.customerService = customerService;
        this.reasonsService = reasonsService;
        this.peopleRepository = peopleRepository;
    }

    public List<Reason> findAllExWithResponse() {
        return this.findAllEx().stream().toList();
    }


    @Transactional
    public List<Reason> addExcludedReasons(Set<Integer> ids) {
        Person person = customerService.getPersonFromAuth();
        Set<Reason> allFromDB = reasonsService.findByIdIn(ids);
        person.getExcludedReasons().addAll(allFromDB);
        return person.getExcludedReasons().stream().toList();
    }

    @Transactional
    public void deleteEx(int reasonId) {
        Person person = customerService.getPersonFromAuth();
        Reason fromDB = reasonsService.findById(reasonId);
        person.getExcludedReasons().remove(fromDB);
        peopleRepository.save(person);
    }

    public Set<Reason> findAllEx() {
        Person person = customerService.getPersonFromAuth();
        return person.getExcludedReasons();
    }
}
