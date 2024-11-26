package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.assay.models.Person;
import project.assay.models.PersonIndicator;
import project.assay.models.PersonIndicatorId;

@Repository
public interface PersonIndicatorRepository extends JpaRepository<PersonIndicator, PersonIndicatorId> {
  @Query("SELECT pi FROM PersonIndicator pi WHERE pi.id.personId = :personId")
  List<PersonIndicator> findByPersonId(@Param("personId") Integer personId);
}
