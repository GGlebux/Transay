package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.PersonIndicator;

@Repository
public interface MainRepository extends JpaRepository<PersonIndicator, Integer> {
  public List<PersonIndicator> findPersonIndicatorByPersonId(int id);
  @Modifying
  @Query("UPDATE PersonIndicator pi SET pi.currentValue = :value WHERE pi.id = :id")
  public void updateCurrentValueById(@Param("id") int id, @Param("value") double value);
}
