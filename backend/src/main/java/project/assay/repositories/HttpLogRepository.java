package project.assay.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.HttpLog;

@Repository
public interface HttpLogRepository extends JpaRepository<HttpLog, Long> {

    Page<HttpLog> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<HttpLog> findByErrorTrueOrderByCreatedAtDesc(Pageable pageable);
}
