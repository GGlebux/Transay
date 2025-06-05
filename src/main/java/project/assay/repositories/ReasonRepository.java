package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Reason;

import java.util.List;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Integer> {
  List<Reason> findAll();
}
