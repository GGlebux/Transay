package project.transay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transay.models.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
}
