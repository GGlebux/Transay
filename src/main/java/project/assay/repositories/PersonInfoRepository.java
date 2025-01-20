package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.PersonInfo;

@Repository
public interface PersonInfoRepository extends JpaRepository<PersonInfo, Integer> {
  @EntityGraph(attributePaths = {"person"})
  List<PersonInfo> findByPersonId(int personId);
}
