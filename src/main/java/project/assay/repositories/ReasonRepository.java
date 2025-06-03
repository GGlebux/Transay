package project.assay.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.ExcludedReason;

@Repository
public interface ReasonRepository extends JpaRepository<ExcludedReason, Integer> {

  List<ExcludedReason> findByOwnerId(int personId);
}
