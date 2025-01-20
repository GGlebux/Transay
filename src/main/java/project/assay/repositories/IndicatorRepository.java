package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {
  Indicator getIndicatorById(int Id);

  @EntityGraph(attributePaths = {"personInfo"})
  @Query("SELECT i FROM Indicator i WHERE (i.gender = :gender or i.gender = 'both') and :age BETWEEN i.minAge and i.maxAge")
  List<Indicator> findAllCorrect(@Param("gender") String gender,
      @Param("age") Integer age);
}
