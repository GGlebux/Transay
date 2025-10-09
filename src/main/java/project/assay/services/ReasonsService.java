package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

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
    public ResponseEntity<List<Reason>> findAllWithResponse() {
        return ok(repo.findAll());
    }

    public ResponseEntity<Reason> findByIdWithResponse(int id) {
        return ok(this.findById(id));
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
    public ResponseEntity<Reason> create(String name) {
        return status(CREATED)
                .body(repo.save(new Reason(name)));
    }

    @Transactional
    public ResponseEntity<Reason> update(String name, int id) {
        Reason reason = this.findById(id);
        reason.setName(name);
        return ok(repo.save(reason));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        Reason reason = this.findById(id);
        repo.delete(reason);
        return status(NO_CONTENT).build();
    }
}
