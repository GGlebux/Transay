package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
}
