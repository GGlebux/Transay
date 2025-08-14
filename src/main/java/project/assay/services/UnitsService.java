package project.assay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.models.Unit;
import project.assay.repositories.UnitRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UnitsService {
    private final UnitRepository unitRepository;

    @Autowired
    public UnitsService(UnitRepository unitRepository) {

        this.unitRepository = unitRepository;
    }

    public List<String> findAll() {
        return unitRepository
                .findAll()
                .stream()
                .map(Unit::getName)
                .toList();
    }
}
