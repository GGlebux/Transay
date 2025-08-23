package project.assay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.IndicatorGroup;

@Repository
public interface IndicatorGroupRepository extends JpaRepository<IndicatorGroup, Integer> {
}
