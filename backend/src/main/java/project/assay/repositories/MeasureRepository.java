package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Measure;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Integer> {
  @EntityGraph(attributePaths = {"indicator", "referent"})
  List<Measure> findByPersonId(Long personId);
}
