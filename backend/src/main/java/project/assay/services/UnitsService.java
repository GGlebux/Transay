package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.models.Unit;
import project.assay.repositories.UnitRepository;

import java.util.List;

import static java.lang.String.format;
import static java.util.Comparator.comparing;

@Service
@Transactional(readOnly = true)
public class UnitsService {
    private final UnitRepository repository;

    @Autowired
    public UnitsService(UnitRepository repository) {

        this.repository = repository;
    }

    public List<Unit> findAll() {
        return repository
                .findAll()
                .stream()
                .sorted(comparing(Unit::getName))
                .toList();
    }

    public Unit findById(int id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                    format("Единица измерения с id='%d' не найдена!",
                            id)));
    }


    @Transactional
    public Unit update(int unitId, String name) {
        Unit existed = findById(unitId);
        existed.setName(name);
        return repository.save(existed);
    }

    @Transactional
    public Unit save(String name) {
        return repository.save(new Unit(name));
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }
}
