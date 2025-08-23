package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Unit;
import project.assay.repositories.UnitRepository;

import java.util.List;

import static java.util.Comparator.comparing;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Service
@Transactional(readOnly = true)
public class UnitsService {
    private final UnitRepository repository;

    @Autowired
    public UnitsService(UnitRepository repository) {

        this.repository = repository;
    }

    public ResponseEntity<List<Unit>> findAll() {
        return ok(repository
                .findAll()
                .stream()
                .sorted(comparing(Unit::getName))
                .toList());
    }

    public Unit findById(int id) {
        return repository.findById(id).orElseThrow(()-> new EntityNotFoundException("Unit with id=" + id + " not found"));
    }


    @Transactional
    public ResponseEntity<Unit> update(int unitId, String name) {
        Unit existed = findById(unitId);
        existed.setName(name);
        return ok(repository.save(existed));
    }

    @Transactional
    public ResponseEntity<Unit> save(String name) {
        return ok(repository.save(new Unit(name)));
    }

    @Transactional
    public ResponseEntity<HttpStatus> delete(int id) {
        repository.deleteById(id);
        return status(NO_CONTENT).build();
    }
}
