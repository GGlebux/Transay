package project.assay.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Transcript;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {

  Optional<Transcript> findByName(String name);

}
