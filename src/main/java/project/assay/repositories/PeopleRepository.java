package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.assay.models.Indicator;
import project.assay.models.Person;
import project.assay.models.PersonIndicator;
@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

  public List<PersonIndicator> getPeopleIndicatorById(int personId);
}
