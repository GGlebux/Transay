package project.transay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.transay.models.Reason;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Integer> {
  List<Reason> findAll();
  Optional<Reason> findByName(String name);
  Set<Reason> findByNameIn(Set<String> names);
}
