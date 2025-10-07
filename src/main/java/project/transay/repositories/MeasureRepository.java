package project.transay.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.transay.models.Measure;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Integer> {
  @EntityGraph(attributePaths = {"indicator", "referent"})
  List<Measure> findByPersonId(int personId);

  @EntityGraph(attributePaths = {"person", "referent", "indicator"})
  @Query("SELECT m FROM Measure m WHERE (m.person.id = :personId and m.referent.regDate = :regDate)")
  List<Measure> findByPersonIdAndDate(@Param("personId") int personId,
                                      @Param("regDate") LocalDate regDate);
}
