package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Reason;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Integer> {

  List<Reason> findByOwnerId(int personId);
}
