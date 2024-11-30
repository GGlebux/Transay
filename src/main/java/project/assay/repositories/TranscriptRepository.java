package project.assay.repositories;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.assay.models.Transcript;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, Integer> {
  public Optional<Transcript> findByName(String name);

}
