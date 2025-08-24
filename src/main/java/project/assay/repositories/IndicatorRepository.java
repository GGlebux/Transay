package project.assay.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {

  Optional<Indicator> getIndicatorById(int Id);

  @EntityGraph(attributePaths = {"measure"})
  @Query("SELECT i FROM Indicator i " +
          "WHERE i.rusName = :name " +
          "AND (i.gender = :gender or i.gender = 'both') " +
          "AND i.isGravid = :isGravid " +
          "AND :age BETWEEN i.minAge and i.maxAge")
  List<Indicator> findAllCorrect(@Param(("name")) String name,
                                 @Param("gender") String gender,
                                 @Param("isGravid") Boolean isGravid,
                                 @Param("age") Integer age);
}
