package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Reason;
import project.assay.repositories.ReasonRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
public class ReasonsService {
    private final ReasonRepository repo;

    @Autowired
    public ReasonsService(ReasonRepository repo) {
        this.repo = repo;
    }

    public Reason findById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Причина с id='%d' не найдена!", id)));
    }

    @Transactional
    public Set<Reason> findByIdIn(Set<Integer> ids) {
        return new TreeSet<>(repo.findAllById(ids));
    }

    @Transactional
    public List<Reason> findAllWithResponse() {
        return repo.findAll();
    }

    public Reason findByIdWithResponse(int id) {
        return this.findById(id);
    }

    public Reason findByName(String name) {
        return repo
                .findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        format("Причина c именем='%s' не найдена!", name)));
    }

    @Transactional
    public Set<Reason> findByNameIn(Set<String> names) {
        List<Reason> byNameIn = repo.findByNameIn(names);
        return new HashSet<>(byNameIn);
    }

    @Transactional
    public Reason create(String name) {
        return repo.save(new Reason(name));
    }

    @Transactional
    public Reason update(String name, int id) {
        Reason reason = this.findById(id);
        reason.setName(name);
        return repo.save(reason);
    }

    @Transactional
    public void delete(int id) {
        Reason reason = this.findById(id);
        repo.delete(reason);
    }
}
